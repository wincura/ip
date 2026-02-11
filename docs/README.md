# InvictaBot User Guide

>“By failing to prepare, you are preparing to fail.” – Benjamin Franklin ([source](https://www.goodreads.com/quotes/15061-by-failing-to-prepare-you-are-preparing-to-fail))

>Feminine form of 'invictus' from Latin 'in-' ("not") +‎ 'victus', from vincō (“conquer”), meaning "UNDEFEATED"

Invicta is a simple, easy-to-use chatbot that allows you to be undefeated by your tasks by staying on top of them.  

It is:
- Text-based
- Easy to learn
- ~~EASY~~ SUPER EASY to use

// Product screenshot goes here

## Getting Help

---

You can display the list of commands and their usages.

Usage: `help`

Expected Outcome:

```
List of commands in InvictaBot:
	bye - exit app
	list - display task list
	delete - delete the task
	mark <index> - mark task as done
	unmark <index> - mask task as not done  
	... 
```

## Adding Tasks  

---

### To-do Tasks

You can add simple to-do tasks with a task description and a completion status.

Usage: `todo <name>`    

Example: `todo Buy Books`

Expected Outcome:

```
	Okay! I've added this task: 
		[T][ ] Buy Books
	You've got 1 tasks in your list now.
```

### Tasks with Deadlines

You can add tasks with a task description, a deadline and a completion status.  
The deadline can be a date or a date and time.

Usage: `deadline <name> /by <deadline>`

Example 1: `deadline Homework 1 /by 2026-02-20`  


Expected Outcome:

```
	Okay! I've added this task: 
		[D][ ] Homework (by: Feb 20 2026 (Fri) 12:00 am )
	You've got 2 tasks in your list now.
```
Example 2: `deadline Homework 2 /by 2026-02-20 18:00`  

Expected Outcome:
```
	Okay! I've added this task: 
		[D][ ] Homework 2 (by: Feb 20 2026 (Fri) 06:00 pm )
	You've got 3 tasks in your list now.
```

### Events

You can add events with a task description, a start time, an end time and a completion status.  
The start and end times can also be dates or dates and times.

Usage: `event <name> /from <start> /to <end>`

Example 1: `event CCA Briefing /from 2026-03-27 18:30 /to 2026-03-27 20:30`


Expected Outcome:

```
	Okay! I've added this task: 
		[E][ ] CCA Briefing (from: Mar 27 2026 (Fri) 06:30 pm  to: Mar 27 2026 (Fri) 08:30 pm )
	You've got 4 tasks in your list now.
```

## Listing tasks

---

You can display all tasks with their details including their respective index, task type, and completion status.

Usage: `list`

Expected Outcome:

```
	Here is a list of your tasks: 
	1. [T][ ] Buy Books
	2. [D][ ] Homework 1 (by: Feb 20 2026 (Fri) 12:00 am )
	3. [D][ ] Homework 2 (by: Feb 20 2026 (Fri) 06:00 pm )
	4. [E][ ] CCA Briefing (from: Mar 27 2026 (Fri) 06:30 pm  to: Mar 27 2026 (Fri) 08:30 pm )
	...
```

## Editing Tasks

---

### Marking Tasks

You can mark a task as complete using its respective index in the task list.

Usage: `mark <index>`

Example: `mark 1`

Expected Outcome:

```
	Great! I've marked this as done:  
		[T][X] Buy Books
```

### Unmarking Tasks

You can mark a task as incomplete using its respective index in the task list.

Usage: `unmark <index>`

Example: `unmark 1`

Expected Outcome:

```
	Oh I see! I've marked this as not done: 
		[T][ ] Buy Books
```

### Deleting tasks

You can delete a task using its respective index in the task list.

Usage: `delete <index>`  
  
Example: `delete 3`

Expected Outcome:

```
	Into the trash! This task has been deleted: 
		[D][ ] Homework 2 (by: Feb 20 2026 (Fri) 06:00 pm )
	You've got 3 tasks in your list now.
```

## Searching Tasks

---

### Find Matching Tasks

You can search for tasks in the task list containing the search string.

Usage: `find <search string>`

Example: `find CCA`

Expected Outcome:
```
	Here is a list of your tasks that contains 'CCA' : 
	1. [E][ ] CCA Recruitment Drive (from: Feb 19 2026 (Thu) 06:30 pm  to: Mar 28 2026 (Sat) 08:30 pm )
	2. [E][ ] CCA Event (from: Feb 21 2026 (Sat) 06:30 pm  to: Feb 21 2026 (Sat) 08:30 pm )
	3. [E][ ] CCA Meeting (from: Feb 25 2026 (Wed) 10:30 am  to: Feb 25 2026 (Wed) 08:30 pm )
	4. [E][ ] CCA Festival (from: Mar 21 2026 (Sat) 06:30 pm  to: Mar 21 2026 (Sat) 08:30 pm )
	5. [E][ ] CCA Briefing (from: Mar 27 2026 (Fri) 06:30 pm  to: Mar 27 2026 (Fri) 08:30 pm )
```

### Find Tasks within a Period

You can search for tasks in the task list that fall within a given period.

Usage: `period /from <start time> /end <end>`

Example: `period /from 2026-02-21 /to 2026-03-23`  

Expected Outcome:
```
	Here is a list of your tasks that fall within Feb 21 2026 (Sat) to Mar 23 2026 (Mon): 
	1. [D][ ] Quiz 1 (by: Feb 21 2026 (Sat) 11:59 pm )
	2. [D][ ] Problem Set 1 (by: Feb 21 2026 (Sat) 11:59 pm )
	3. [E][ ] CCA Recruitment Drive (from: Feb 19 2026 (Thu) 06:30 pm  to: Mar 28 2026 (Sat) 08:30 pm )
	4. [E][ ] CCA Event (from: Feb 21 2026 (Sat) 06:30 pm  to: Feb 21 2026 (Sat) 08:30 pm )
	5. [E][ ] CCA Meeting (from: Feb 25 2026 (Wed) 10:30 am  to: Feb 25 2026 (Wed) 08:30 pm )
	6. [E][ ] CCA Festival (from: Mar 21 2026 (Sat) 06:30 pm  to: Mar 21 2026 (Sat) 08:30 pm )
```
### Find Tasks on a Day

You can search for tasks in the task list that fall on a given date.

Usage: `day <date>`

Example: `day 2026-02-21`

Expected Outcome:
```
	Here is a list of your tasks that you have on Feb 21 2026 (Sat): 
	1. [D][ ] Quiz 1 (by: Feb 21 2026 (Sat) 11:59 pm )
	2. [D][ ] Problem Set 1 (by: Feb 21 2026 (Sat) 11:59 pm )
	3. [E][ ] CCA Recruitment Drive (from: Feb 19 2026 (Thu) 06:30 pm  to: Mar 28 2026 (Sat) 08:30 pm )
	4. [E][ ] CCA Event (from: Feb 21 2026 (Sat) 06:30 pm  to: Feb 21 2026 (Sat) 08:30 pm )
```

## Exiting the App

---

You can exit the app by telling it goodbye.

Usage: `bye`

Expected Outcome:

```
	Bye bye now! You take care, <your username>!
```
## Acknowledgements

---

Logo for chatbot generated with the help of an external Text to ASCII Art Generator tool from https://patorjk.com/software/taag/.
