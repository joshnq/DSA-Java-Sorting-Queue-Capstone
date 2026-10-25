import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);
    private static final ArrayList<Integer> requestIds = new ArrayList<Integer>();
    private static final ArrayList<String> studentIds = new ArrayList<String>();
    private static final ArrayList<String> studentNames = new ArrayList<String>();
    private static final ArrayList<String> bookTitles = new ArrayList<String>();
    private static final ArrayList<Integer> publicationYears = new ArrayList<Integer>();
    private static final ArrayList<String> requestStatuses = new ArrayList<String>();
    private static final Queue<Integer> requestQueue = new LinkedList<Integer>();
    private static int nextRequestId = 1;

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("=== Library Borrowing Request System ===");
        while (running) {
            showMenu();
            int choice = readInt("Choose an option: ", 0, 11);
            switch (choice) {
                case 1: addRequest(); break;
                case 2: displayRequests(); break;
                case 3: searchRequests(); break;
                case 4: bubbleSortByBookTitle(); break;
                case 5: insertionSortByStudentName(); break;
                case 6: enqueueExistingRequest(); break;
                case 7: dequeueRequest(); break;
                case 8: peekRequest(); break;
                case 9: displayQueue(); break;
                case 10: checkQueue(); break;
                case 11: updateRequest(); break;
                case 0: running = false; System.out.println("Goodbye. Thank you for using the system."); break;
                default: System.out.println("Invalid menu choice. Enter a number from 0 to 11.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Register borrowing request (automatically joins queue)");
        System.out.println("2. Display all requests");
        System.out.println("3. Search by request ID, student ID, student, or book");
        System.out.println("4. Bubble sort requests by book title");
        System.out.println("5. Insertion sort requests by student name");
        System.out.println("6. Enqueue an existing pending request");
        System.out.println("7. Process next request (dequeue)");
        System.out.println("8. Peek at next request");
        System.out.println("9. Display pending queue");
        System.out.println("10. Check whether queue is empty");
        System.out.println("11. Update request details");
        System.out.println("0. Exit");
    }

    private static void addRequest() {
        System.out.println("\n--- Register Borrowing Request ---");
        String studentId = readText("Student ID: ");
        String studentName = readText("Student name: ");
        String bookTitle = readText("Book title: ");
        int year = readInt("Publication year (1-2026): ", 1, 2026);
        int id = nextRequestId++;
        requestIds.add(id);
        studentIds.add(studentId);
        studentNames.add(studentName);
        bookTitles.add(bookTitle);
        publicationYears.add(year);
        requestStatuses.add("Pending");
        requestQueue.offer(id);
        System.out.println("Request #" + id + " registered and added to the end of the FIFO queue.");
    }

    private static void displayRequests() {
        System.out.println("\n--- All Borrowing Requests ---");
        if (requestIds.isEmpty()) { System.out.println("No borrowing requests have been registered."); return; }
        printHeader();
        for (int i = 0; i < requestIds.size(); i++) printRequest(i);
    }

    private static void searchRequests() {
        if (requestIds.isEmpty()) { System.out.println("No requests to search."); return; }
        String query = readText("Search (request ID, student ID, name, or book title): ").toLowerCase();
        boolean found = false;
        printHeader();
        for (int i = 0; i < requestIds.size(); i++) {
            if (String.valueOf(requestIds.get(i)).equalsIgnoreCase(query)
                    || studentIds.get(i).toLowerCase().contains(query)
                    || studentNames.get(i).toLowerCase().contains(query)
                    || bookTitles.get(i).toLowerCase().contains(query)) {
                printRequest(i); found = true;
            }
        }
        if (!found) System.out.println("No matching request was found.");
    }

    private static void updateRequest() {
        if (requestIds.isEmpty()) { System.out.println("There are no requests to update."); return; }
        int id = readInt("Enter request ID to update: ", 1, Integer.MAX_VALUE);
        int index = findRequestIndex(id);
        if (index < 0) { System.out.println("Request #" + id + " was not found."); return; }
        System.out.println("Enter the replacement details for request #" + id + ".");
        studentIds.set(index, readText("Student ID: "));
        studentNames.set(index, readText("Student name: "));
        bookTitles.set(index, readText("Book title: "));
        publicationYears.set(index, readInt("Publication year (1-2026): ", 1, 2026));
        System.out.println("Request #" + id + " updated. Its queue position and status are unchanged.");
    }

    // Bubble sort orders the catalogue of requests by book title; swapping every parallel list keeps each record intact.
    private static void bubbleSortByBookTitle() {
        if (requestIds.isEmpty()) { System.out.println("No requests to sort."); return; }
        System.out.println("\nBefore Bubble Sort (book title A-Z):"); displayRequests();
        for (int end = bookTitles.size() - 1; end > 0; end--) {
            boolean changed = false;
            for (int i = 0; i < end; i++) {
                if (bookTitles.get(i).compareToIgnoreCase(bookTitles.get(i + 1)) > 0) {
                    swapRecords(i, i + 1); changed = true;
                }
            }
            if (!changed) break;
        }
        System.out.println("After Bubble Sort:"); displayRequests();
        System.out.println("Bubble Sort compares neighboring titles and swaps out-of-order pairs; it is easy to trace for a small request list.");
        System.out.println("The FIFO queue order is unchanged.");
    }

    // Insertion sort places each request into the already-sorted prefix, ordered by student name.
    private static void insertionSortByStudentName() {
        if (requestIds.isEmpty()) { System.out.println("No requests to sort."); return; }
        System.out.println("\nBefore Insertion Sort (student name A-Z):"); displayRequests();
        for (int i = 1; i < studentNames.size(); i++) {
            int j = i;
            while (j > 0 && studentNames.get(j - 1).compareToIgnoreCase(studentNames.get(j)) > 0) {
                swapRecords(j - 1, j); j--;
            }
        }
        System.out.println("After Insertion Sort:"); displayRequests();
        System.out.println("Insertion Sort grows a sorted prefix one request at a time; it works well for small or nearly ordered lists.");
        System.out.println("The FIFO queue order is unchanged.");
    }

    private static void enqueueExistingRequest() {
        if (requestIds.isEmpty()) { System.out.println("There are no registered requests to enqueue."); return; }
        int id = readInt("Enter the pending request ID to enqueue: ", 1, Integer.MAX_VALUE);
        int index = findRequestIndex(id);
        if (index < 0) { System.out.println("Request #" + id + " was not found."); return; }
        if (!requestStatuses.get(index).equals("Pending")) { System.out.println("Only pending requests can be enqueued."); return; }
        if (queueContains(id)) { System.out.println("Request #" + id + " is already in the queue."); return; }
        requestQueue.offer(id);
        System.out.println("Request #" + id + " added to the end of the queue.");
    }

    private static void dequeueRequest() {
        Integer id = requestQueue.poll();
        if (id == null) { System.out.println("The queue is empty; there is no request to process."); return; }
        int index = findRequestIndex(id);
        if (index < 0) { System.out.println("Queue error: request record #" + id + " could not be found."); return; }
        requestStatuses.set(index, "Processed");
        System.out.println("Processed request #" + id + " for " + studentNames.get(index) + " (" + bookTitles.get(index) + ").");
    }

    private static void peekRequest() {
        Integer id = requestQueue.peek();
        if (id == null) { System.out.println("The queue is empty; there is no next request."); return; }
        int index = findRequestIndex(id);
        if (index < 0) { System.out.println("Queue error: request record #" + id + " could not be found."); return; }
        System.out.println("Next request (not removed): #" + id + " | " + studentNames.get(index) + " | " + bookTitles.get(index));
    }

    private static void displayQueue() {
        if (requestQueue.isEmpty()) { System.out.println("The pending request queue is empty."); return; }
        System.out.println("\n--- Pending Queue (front to back; FIFO) ---");
        for (Integer id : requestQueue) {
            int index = findRequestIndex(id);
            if (index >= 0) System.out.println("#" + id + " | " + studentNames.get(index) + " | " + bookTitles.get(index));
        }
    }

    private static void checkQueue() {
        if (requestQueue.isEmpty()) System.out.println("Queue is empty.");
        else System.out.println("Queue is not empty (" + requestQueue.size() + " pending request(s)).");
    }

    private static void printHeader() {
        System.out.printf("%-6s %-14s %-22s %-32s %-8s %-10s%n", "ID", "Student ID", "Student Name", "Book Title", "Year", "Status");
        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }

    private static void printRequest(int i) {
        System.out.printf("%-6d %-14s %-22s %-32s %-8d %-10s%n", requestIds.get(i), studentIds.get(i),
                studentNames.get(i), bookTitles.get(i), publicationYears.get(i), requestStatuses.get(i));
    }

    private static void swapRecords(int first, int second) {
        swapInteger(requestIds, first, second);
        swapString(studentIds, first, second);
        swapString(studentNames, first, second);
        swapString(bookTitles, first, second);
        swapInteger(publicationYears, first, second);
        swapString(requestStatuses, first, second);
    }

    private static void swapInteger(ArrayList<Integer> list, int first, int second) {
        Integer value = list.get(first); list.set(first, list.get(second)); list.set(second, value);
    }

    private static void swapString(ArrayList<String> list, int first, int second) {
        String value = list.get(first); list.set(first, list.get(second)); list.set(second, value);
    }

    private static int findRequestIndex(int id) {
        for (int i = 0; i < requestIds.size(); i++) if (requestIds.get(i) == id) return i;
        return -1;
    }

    private static boolean queueContains(int id) {
        for (Integer queuedId : requestQueue) if (queuedId == id) return true;
        return false;
    }

    private static String readText(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) return value;
            System.out.println("Input cannot be blank. Please try again.");
        }
    }

    private static int readInt(String prompt, int minimum, int maximum) {
        while (true) {
            String value = readLine(prompt);
            try {
                int number = Integer.parseInt(value);
                if (number < minimum || number > maximum) {
                    System.out.println("Enter a whole number from " + minimum + " to " + maximum + ".");
                } else return number;
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input. Enter a whole number.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!input.hasNextLine()) {
            System.out.println("\nInput has ended. The program will now exit safely.");
            System.exit(0);
        }
        return input.nextLine().trim();
    }
}
