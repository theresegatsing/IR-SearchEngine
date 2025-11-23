package searchengine;

import java.util.*;

/**
 * Handles "advanced" queries:
 *
 * Supported syntax:
 *   - simple terms: java search engine
 *       => treated like: java AND search AND engine
 *
 *   - Boolean operators: AND, OR, NOT (case-insensitive)
 *       Example: java AND search NOT python
 *
 *   - Exact phrase: "java search"
 *       => matches documents whose text contains the phrase between quotes
 *
 * Precedence rules (simple version):
 *   - NOT has highest precedence (applies to the next term or phrase)
 *   - AND / OR are evaluated left-to-right
 */

public class BooleanQueryProcessor {
	
	/**
     * Evaluate a raw query string into a set of documents.
     *
     * @param rawQuery the user's query
     * @param allDocs  all documents in the collection
     * @param index    inverted index for fast term lookup
     */
    public static Set<Document> evaluate(String rawQuery,
                                         List<Document> allDocs,
                                         InvertedIndex index) {
        // 1) Parse into tokens where quoted phrases are kept together
        List<String> tokens = tokenizeQuery(rawQuery);

        if (tokens.isEmpty()) {
            return new HashSet<>();
        }

        // 2) Evaluate with simple boolean logic
        Set<Document> result = null;
        String pendingOp = null; // "AND" or "OR"
        boolean negateNext = false;

        for (String token : tokens) {
            String upper = token.toUpperCase();

            // Handle operators first
            if ("AND".equals(upper) || "OR".equals(upper)) {
                pendingOp = upper;
                continue;
            }
            if ("NOT".equals(upper)) {
                negateNext = true;
                continue;
            }

            // token is now either a term or a phrase
            Set<Document> termDocs = getDocsForToken(token, allDocs, index);

            // If we had a NOT before this term, take the complement
            if (negateNext) {
                Set<Document> complement = new HashSet<>(allDocs);
                complement.removeAll(termDocs);
                termDocs = complement;
                negateNext = false;
            }

            if (result == null) {
                // First operand
                result = new HashSet<>(termDocs);
            } else {
                // Combine with previous result using pending operator
                if ("OR".equals(pendingOp)) {
                    result.addAll(termDocs);
                } else {
                    // Default AND if no operator given
                    result.retainAll(termDocs);
                }
            }
        }

        if (result == null) {
            // If query was something weird like only operators, just return empty
            return new HashSet<>();
        }

        return result;
    }


}
