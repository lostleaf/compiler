Compiler 2013 Testcases
=======================

Please send pull requests to submit your testcases.
If you don't use GitHub, use it now.

Before you do anything related to this repository, 
read [this article](http://gun.io/blog/how-to-github-fork-branch-and-pull-request/) carefully,
which teaches you how to GitHub.
A kind advice is to create separate branches for different testcases you're submitting,
because you can only have one pull request per branch.

You only need to submit the C source files.
The testing system will compare the student program's output with gcc's output.

You can submit either good programs which are syntactically and semantically correct,
or bad programs with syntax or semantic errors. 
Both kinds of programs will be tested and judged according to the behavior of gcc.

For syntactically correct programs, please try your best to write it beautifully.
If you're not confident about your coding style, use a tool such as [astyle](http://astyle.sourceforge.net/).
However, you are allowed and encouraged to submit ugly testcases just for challenging buggy parsers.

Name your testcases as "`StudentNumber-StudentName-TestcaseName.c`"
(e.g. "`5090379042-jiaxiao-hello.c`"; without the quotes).
The `TestcaseName` is required to be matched by the regex `/[_a-z0-9]+/`.

You can get a bonus of 0.2 points per merged testcase.
If you can't manage to submit your testcases via GitHub, find a friend for help as soon as possible.
But please try not to ask your teaching assistants for this.
