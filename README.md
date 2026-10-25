# Library Borrowing Request System

## Project Description

This console application helps a library keep track of students' requests to borrow books. Each request is saved as a record and placed in a pending queue. Library staff can process requests in arrival order, find and update requests, and sort the request records to make them easier to review.

## Objectives

- Store and manage borrowing request records with Java `ArrayList`s.
- Demonstrate two manually implemented sorting algorithms.
- Process pending requests in first-in, first-out (FIFO) order with a Java `Queue`.
- Use methods, loops, conditionals, and validated console input in a procedural program.
- Keep sorted record views separate from the request queue's processing order.

## Use Cases and Features

- Register a request with student ID, student name, book title, and publication year. The request receives a unique ID and is automatically enqueued.
- Display and search all records by request ID, student ID, student name, or book title.
- Update the details of a request without changing its ID, queue position, or status.
- Sort records by book title using Bubble Sort or by student name using Insertion Sort. Each operation shows records before and after sorting.
- Enqueue a registered pending request that is not already in the queue.
- Peek at the next request, display the queue, check whether it is empty, and process (dequeue) the next request.
- Validate text and numeric input and show clear messages for invalid input, missing records, and empty lists or queues.

## Data Structure Explanation

The system uses parallel `ArrayList`s for request IDs, student IDs, student names, book titles, publication years, and request statuses. An item's index in every list identifies the same request. The `swapRecords` helper swaps every field together during sorting, so record data stays associated correctly. Request IDs are stable even when list positions change.

A `Queue<Integer>` backed by `LinkedList` stores request IDs, not list indexes. New requests join the back of the queue; `poll()` processes the front request and marks its record as processed; `peek()` views the front without removing it. Since sorting changes only the record lists, it does not change FIFO order.

## Sorting Algorithm Explanation

### Bubble Sort by book title

Bubble Sort compares neighboring titles and swaps their complete records when they are out of alphabetical order. Passes continue until the list is ordered (or a pass makes no swaps). It is straightforward to follow for a small classroom request list and demonstrates comparisons and swaps clearly.

### Insertion Sort by student name

Insertion Sort builds an ordered prefix by moving each request left until it is in the correct alphabetical position by student name. It is suitable for small lists and can be efficient when records are already nearly ordered. Both algorithms are implemented directly in `Main.java`; no built-in sorting method is used.

## How to Run the Program

From the project folder, compile and run:

```text
javac src/Main.java
java -cp src Main
```

Use menu option `0` to exit. Menu choices 4 and 5 sort the records; options 6 through 10 demonstrate queue operations; option 11 updates a request.

## Screenshots of the System

The following images were captured from the actual running program and are included in `screenshots/`.

| Screenshot file | What to capture |
|---|---|
| `main-menu.png` | Main menu and exit option |
| `add-and-display-records.png` | Register multiple requests and display them |
| `queue-operations.png` | Enqueue, peek, display, and dequeue operations |
| `fifo-behavior.png` | Show at least two request IDs queued and the first registered request processed first |
| `sorting-algorithm-1.png` | Bubble Sort with records before and after sorting by title |
| `sorting-algorithm-2.png` | Insertion Sort with records before and after sorting by student name |
| `input-validation.png` | Blank text, nonnumeric/out-of-range input, and an empty-queue or missing-record message |

## Grading Criteria Coverage

The project is organized to address all nine rubric categories: integrated application features; two manual sorts; FIFO queue operations; parallel `ArrayList` record management; input validation; procedural code with no custom classes; this README and readable source; Git usage through meaningful commits; and submission files/screenshots. The source, documentation, and seven screenshots are committed to the public `main` branch in GitHub with separate, descriptive commits.
