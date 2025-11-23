package searchengine;

import java.util.*;

/**
 * Inverted index maps terms -> documents that contain those terms.
 *
 * For example:
 *   "java" -> {doc1, doc3}
 *   "search" -> {doc2}
 */

public class InvertedIndex {
	
	// term (lowercase) -> set of documents that contain that term
    private final Map<String, Set<Document>> index = new HashMap<>();
    
    
    /**
     * Add a document to the index.
     * We tokenize the title + content, normalize to lowercase, and map each term to this document.
     */
    public void addDocument(Document doc) {
        String fullText = (doc.getTitle() + " " + doc.getContent()).toLowerCase();

        // Simple tokenization: replace non-alphanumeric by spaces, then split
        String[] tokens = fullText.replaceAll("[^a-z0-9]+", " ").split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            index.computeIfAbsent(token, k -> new HashSet<>()).add(doc);
        }
    }


}
