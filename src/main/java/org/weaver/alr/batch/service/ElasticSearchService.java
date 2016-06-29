package org.weaver.alr.batch.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchService {
	
	public static final Integer SORT_DESC	= 0;
	public static final Integer SORT_ASC	= 1;

	@Autowired
	Client esClient;
	
	public <T> T getDocument(String index, String docType, String docId, Class<T> clazz) {
		GetResponse response = esClient
				.prepareGet(index, docType, docId)
				.setOperationThreaded(false)
				.get();
		if(response != null && response.getSource() != null) {
			try {
				return map2obj(response.getSource(), clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public long putDocument(String index, String docType, String docId, Object doc) {
		try {
			IndexResponse response = esClient
					.prepareIndex(index, docType, docId)
					.setSource(obj2map(doc))
					.get();
			if(response != null) {
				return response.getVersion();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public long deleteDocument(String index, String docType, String docId) {
		DeleteResponse response = esClient
				.prepareDelete(index, docType, docId)
				.get();
		if(response != null) {
			return response.getVersion();
		}
		return 0;
	}
	
	public long updateDocument(String index, String docType, String docId, Object doc) {
		try {
			UpdateResponse response = esClient
					.prepareUpdate(index, docType, docId)
					.setDoc(obj2map(doc))
					.get();
			if(response != null) {
				return response.getVersion();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public long updateDocumentByScript(String index, String docType, String docId, String script) {
		UpdateResponse response = esClient
				.prepareUpdate(index, docType, docId)
				.setScript(new Script(script))
				.get();
		if(response != null) {
			return response.getVersion();
		}
		return 0;
	}
	
	public long upsertDocument(String index, String docType, String docId, Object doc) {
		try {
			UpdateResponse response = esClient.prepareUpdate(index, docType, docId)
					.setDoc(obj2map(doc))
					.setUpsert(obj2map(doc))
					.get();
			if(response != null) {
				return response.getVersion();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public <T> List<T> searchDocument(String index, String docType, Map<String,String> fields,
			Integer from, Integer size,
			String sortField, Integer sortOrder,
			Class<T> clazz) {
		List<T> docList = new ArrayList<T>();
		QueryBuilder qb = null;
		SortBuilder sb = null;
		
		if(from == null) {
			from = 0;
		}
		if(size == null) {
			size = 100;
		}

		if(sortField != null && sortOrder != null) {
			sb = SortBuilders.fieldSort(sortField);
			if(sortOrder == SORT_DESC) {
				sb.order(SortOrder.DESC);
			}
			else if(sortOrder == SORT_ASC) {
				sb.order(SortOrder.ASC);
			}
		}
		else {
			sb = SortBuilders.scoreSort();
		}

		if(fields != null && fields.size() > 0) {
			qb = QueryBuilders.boolQuery();
			for(Entry<String, String> e: fields.entrySet()) {
				((BoolQueryBuilder)qb).must(QueryBuilders.termQuery(e.getKey(), e.getValue()));
			}
		}
		else {
			qb = QueryBuilders.matchAllQuery();
		}

		SearchResponse response = null;
		try {
			response = esClient
				.prepareSearch(index)
				.setTypes(docType)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(qb)
				.setFrom(from).setSize(size)
				.addSort(sb)
				.get();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		if(response != null) {
			for(SearchHit hit: response.getHits().getHits()) {
				try {
					docList.add(map2obj(hit.getSource(), clazz));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return docList;
	}
	
	public long countDocument(String index, String docType, Map<String,String> fields) {
		QueryBuilder qb = null;
		
		qb = QueryBuilders.boolQuery();
		for(Entry<String, String> e: fields.entrySet()) {
			((BoolQueryBuilder)qb).must(QueryBuilders.termQuery(e.getKey(), e.getValue()));
		}
		
		SearchResponse response = esClient
				.prepareSearch(index)
				.setTypes(docType)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(qb)
				.setFrom(0).setSize(0)
				.get();
		
		if(response != null) {
			return response.getHits().getTotalHits();
		}
		return 0;
	}
	
	private <T> T map2obj(Map<String,Object> map, Class<T> clazz) throws InstantiationException, IllegalAccessException {
		T obj = clazz.newInstance();
		for(Field f: clazz.getDeclaredFields()) {
			Object value = map.get(f.getName());
			if(value != null) {
				f.setAccessible(true);
				f.set(obj, value);
			}
		}
		return obj;
	}
	
	private Map<String,Object> obj2map(Object obj) throws IllegalArgumentException, IllegalAccessException {
		Map<String,Object> map = new HashMap<String,Object>();
		
		for(Field f: obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			map.put(f.getName(), f.get(obj));
		}
		return map;
	}
	
}
