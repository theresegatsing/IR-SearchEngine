package searchengine;

import java.util.*;

/**
 * High-level search engine:
 *   - holds documents
 *   - builds an inverted index
 *   - takes user queries, uses BooleanQueryProcessor
 *   - scores and sorts results
 */

public class SimpleSearchEngine {
	
	private final List<Document> documents;
    private final InvertedIndex index;

}
