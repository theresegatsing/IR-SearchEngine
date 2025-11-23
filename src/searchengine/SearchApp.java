package searchengine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console application entry point.
 *
 * This class:
 *   - creates some sample documents (in a real project, Crawl API would fetch these)
 *   - builds the SimpleSearchEngine
 *   - reads user queries from stdin
 *   - prints results with score, date, and snippet
 */

public class SearchApp {
	
	public static void main(String[] args) {
        // 1) Prepare some example documents
        List<Document> docs = createSampleDocuments();

        // 2) Build search engine
        SimpleSearchEngine engine = new SimpleSearchEngine(docs);

        System.out.println("========================================");
        System.out.println("      Simple Search Engine (Java)");
        System.out.println("========================================");
        System.out.println("Indexed " + docs.size() + " documents.");
        System.out.println("You can use:");
        System.out.println("  - simple keywords: java search");
        System.out.println("  - Boolean operators: AND, OR, NOT");
        System.out.println("  - exact phrases: \"search engine\"");
        System.out.println("Type 'exit' to quit.\n");

        // 3) Read queries in a loop
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter your query: ");
                String query = scanner.nextLine().trim();

                if (query.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    break;
                }
                if (query.isEmpty()) {
                    System.out.println("Please type something.\n");
                    continue;
                }

                System.out.print("Sort by date as well? (y/n): ");
                String sortInput = scanner.nextLine().trim();
                boolean sortByDate = sortInput.equalsIgnoreCase("y");

                // 4) Perform search
                List<SearchResult> results = engine.search(query, sortByDate);

                // 5) Display results
                if (results.isEmpty()) {
                    System.out.println("No results found.\n");
                } else {
                    System.out.println("\nFound " + results.size() + " result(s):\n");
                    int rank = 1;
                    for (SearchResult result : results) {
                        Document doc = result.getDocument();
                        System.out.println("[" + rank + "] " + doc.getTitle());
                        System.out.println("    ID: " + doc.getId() +
                                           " | Date: " + doc.getDate() +
                                           " | Score: " + String.format("%.2f", result.getScore()));
                        System.out.println("    Snippet: " + result.getSnippet());
                        System.out.println();
                        rank++;
                    }
                }
            }
        }
    }


}
