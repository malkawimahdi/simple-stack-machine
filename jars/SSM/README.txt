This version of the SSM library includes support for alternative text encodings.
The Assemble and Run programs now allow you to specify a text encoding as a
command-line option, for example:

   java Assemble -charset=UTF-16 myFile

If you run Assemble _without_ this option, it will do its best to figure out an
encoding which works for myFile. This will probably work most of the time.

This change addresses an issue that arises when using the redirect operator (>) in
some versions of powershell. In these versions of powershell, when you do this

   someCommand > myFile

the output from someCommand is first decoded as text and then re-encoded _using a
different encoding_ as it is written into myFile. The re-encoding uses
UTF-16 (specifically, UTF-16LE with BOM). The previous versions of the SSM assembler
assume that the input file is encoded using the platform-default encoding.
Since UTF-16LE is _not_ the default encoding for the Windows JVM, the assembler will
fail when it attempts to process a file which has been created via a > redirect in
these versions of powershell.
