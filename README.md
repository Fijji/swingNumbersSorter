Виконайте завдання на Swing саме так, як описано у т. з.
Не потрібно створювати зайві сутності. У коді має бути лише те, що необхідно. Один клас із 200-300 (можливо трохи
більше) акуратних рядків коду. Для сортування використовуйте тільки алгоритм Quicksort.
Проєкт має збиратися на Maven або Gradle.

Create a single page application. The app has 2 main screens, Intro &amp; Sort.
Numbers buttons:
• Show X random numbers (depends on data entered by user in the previous screen)
• The max number value is 1000
• At least one value should be equal or less than 30
• Present maximum 10 numbers in a column. If there are more numbers, add another
column
Sort button:
• Clicking the sort button will sort the presented numbers in descending order
• Clicking the sort button again, will change it to increasing order
• The screen should be updated after each iteration of quick-sort (i.e. re-implement quick-
sort; copy/paste of existing implementation is permitted).
Reset button:
Takes back to intro screen.
Clicking one of the numbers button:
• If the clicked value is equal or less than 30, present X new random numbers on the
screen
• If the clicked value is more than 30, pop up a message “Please select a value smaller or
equal to 30.”