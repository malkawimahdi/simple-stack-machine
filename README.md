# SSM: The Simple Stack Machine

## Overview

The Simple Stack Machine is a stack-based abstract machine, similar in style to (but much simpler
than) the Java Virtual Machine. An SSM program is a sequence of 32-bit integers in memory
starting at address 0. The SSM starts with an empty operand stack and an empty call-stack. When
the call instruction is executed, a new frame is pushed on the call-stack, and a new empty
operand stack is created and becomes active. When the ret or retv instruction is executed, the
topmost frame of the call-stack is popped, the associated operand stack is discarded, and the
previously active operand stack becomes active again. All machine instructions have a 32-bit
opcode and take their operands from the currently active operand stack (except for the push
instruction, whose operand is the following integer in the code sequence). There are three
programmable registers.

Below is an example execution of the stack machine when the binary program is the following
sequence of integers:

```
2, 7, 2, 16 , 12, 2, 6 , 7, 6, 2, 3 , 14, 2, 2, 14, 0
```
(assembly code mnemonics are shown below). The operand stack (initially empty) is shown in
angle brackets, with elements listed from bottom to top:
→ <>
push 7 → < 7 >
push 11 → <7, 11 >
load → <7, 14 > // value 14 loaded from memory address 11
push 9 → <7, 14 , 9 >
sub → <7, 5 >
add → < 12 >
push 3 → < 12 , 3 >
sysc → <> // syscall 3 ; outputs decimal representation of an integer
push 2 → < 2 >
sysc → <> // syscall 2 ; outputs a line ending
halt

All opcodes and operands are represented as 32-bit integers in two’s-complement format. Clearly,
this is extremely wasteful of space ( 5 bits would suffice for the opcodes, since there are only 2 6 of
them) but it keeps things simple.


## Registers

**lp** (low-memory pointer, read-only):
_Address of the first word of dynamically-allocated memory._
**sp** (stack pointer):
_Address of the top of the call-stack (grows downwards)_.
Note: the call-stack is implemented in dynamically-allocated memory; the operand stack is
a separate entity and each frame on the call-stack is associated with its own operand stack.
**fp** (frame pointer):
_Conventionally used to store the address of an “anchor” point in the top-most frame on the
call-stack._

## Memory layout

```
Main memory
```
```
Statically-allocated data
and command-line arguments
```
```
Code
(read-only memory)
```
```
Call stack
(grows downwards)
```
```
Memory address 0
```
```
lp →
```
```
High memory address
```
```
sp →
```

## Instruction set

The role played by the operand stack for each instruction is presented in a style similar to the style
used in the JVM specification. For example, the entry for opcode 7 (sub) indicates that this
instruction pops _v2_ and _v1_ off the operand stack and pushes the value _v1_ - _v_.

```
Opcode Mnemonic Operand stack Description

0 halt (^) ... → ... Halts the machine.

1 noop (^) ... → ... Does nothing (“no op”).

(^2) push _n_ (^) ... → ..., _n_ Push an integer.

3 pop (^) ..., _v_ → ... Pop the operand stack.

4 dup (^) ..., v → ..., _v, v_ Duplicate the top element.

5 swap (^) ..., _v 1 , v 2_ → ..., _v 2_ , _v 1_ Swap the top two elements.

6 add (^) ..., _v 1 , v 2_ → ..., ( _v 1_ + _v2)_ Integer addition.

7 sub (^) ..., _v1, v2_ → ..., ( _v 1 – v2)_ Integer subtraction.

8 mul (^) ..., _v1, v2_ → ..., ( _v1 * v2)_ Integer multiplication.

9 div (^) ..., _v1, v2_ → ..., ( _v1 / v2)_ Integer division.

10 test_z (^) ..., v → ..., _b b_ = 1 if _v_ = 0
_b_ = 0 otherwise

11 test_n (^) ..., v → ..., _b_ b = 1 if _v_ < 0
_b_ = 0 otherwise

12 load (^) ..., _addr_ → ..., _v_ Load value from memory.
_v_ = mem[ _addr_ ]

13 store (^) ..., _v_ , _addr_ → ... Store value in memory.
mem[ _addr_ ] = _v_

14 sysc (^) System call. See separate table.

15 jump (^) ..., _addr_ → ... Jump to code at address _addr_.

16 jump_z (^) ..., _v_ , _addr_ → ... Conditional jump: jump to code at
address _addr_ only if _v_ is zero.

17 jump_n (^) ..., _v_ , _addr_ → ... Conditional jump: jump to code at
address _addr_ only if _v_ is negative.

18 get_lp (^) ... → ..., _n_ Push value of low-memory pointer.

19 set_fp (^) ..., _v_ → ... Set value of frame pointer.

20 get_fp (^) ... → ..., _n_ Push value of frame pointer.

21 set_sp (^) ..., _v_ → ... Set value of call-stack pointer.

22 get_sp (^) ... → ..., _n_ Push value of call-stack pointer.

23 call (^) ..., _addr_ , _v1,...,vn, n_ → ..., _rv_ Push _n_ values on the call-stack. Open a
new operand stack, jump to _addr._
(Return value _rv_ is pushed on return.)

24 ret (^) ..., _addr, n_ → [empty] Pop _n_ items from the call-stack. Restore
previous operand stack, jump to _addr_.

25 ret_v (^) ..., _rv_ , _addr, n_ → [empty] Pop _n_ items from the call-stack. Restore
previous operand stack, push _rv_ on
restored operand stack, jump to _addr_.

```

## System calls

```
Opcode Mnemonic Operand stack Description
```
### 14

```
sysc
```
```
..., b , 0 → ... OUT_BYTE. Write byte b^ to stdout.^
..., v , 1 → ... OUT_CHAR. Output character with
code v.
..., 2 → ... OUT_LN. Output a line ending.
..., v , 3 → ... OUT_DEC. Output decimal
representation of integer v.
..., addr , 4 → ... OUT_STR. Output string stored at
address addr (strings are stored as
arrays of character codes).
..., 5 → ..., b READ. Read a byte b from stdin and
push on operand stack (push -1 if end
of stdin has been reached).
..., 6 → ..., argc PUSH_ARGC. Push argc^ on the operand
stack, argc is the number of command-
line arguments which were provided
when the binary was loaded.
..., n, 7 → ..., addr PUSH_ARG. Push addr^ on the operand
stack, where addr is the address in
memory of command-line argument
number n. Note: argument number 0 is
usually the name of the binary file.
```

## Assembly Language

SSM programs are typically defined in assembly language, using human-readable mnemonics in
place of numeric op codes and labels in place of numeric memory addresses. Example:

In this example, label **$loop** will resolve to memory address **0** , label **$end** will resolve to memory
address **22** , and label **x** will resolve to memory address **26**. A label can be any non-empty
sequence of digits, letters, underscores and dollar signs, except it cannot start with a digit and it
must be distinct from all of the instruction names.

Notes:

- The integer operand for push can be specified by either a label name, an integer literal, or
    a character literal (in single quotes). A character literal is just shorthand for its
    corresponding Unicode codepoint (eg 'Z'is the same as 90).
- The .data section after the code section is optional.
- As well as integer and character literals, data definitions in the .data section can also use
    string literals (in double quotes). Strings are represented in memory as arrays, ie sequences
    of integers, where the first integer gives the length of the string (eg "ZZ" allocates three
    words of memory initialised to contain the integers 2, 90, 90).
- It is also permitted for an instruction or data definition to have multiple labels, all of which
    will resolve to the same memory address, but each label must be on a separate line.

```
// a simple SSM assembly program
// label names ($loop, $end, x) will be resolved
// to memory addresses by the assembler
```
```
// operand stack, initially empty
// (elements listed bottom to top)
// <>
$loop: push x // < 26 >
load // <mem[ 26 ]>
push $end // <mem[ 26 ], 22 >
jump_z // <> {jump to $end if mem[x]==0}
push x // < 26 >
load // <mem[ 26 ]>
dup // <mem[ 26 ], mem[ 26 ]>
push 3 // <mem[ 26 ], mem[ 26 ], 1>
sysc // <mem[ 26 ] {output mem[x] as decimal}
push 1 // <mem[ 26 ], 1
sub // <mem[ 26 ]- 1
push x // <mem[ 26 ]-1, 26
store // <> {mem[x]=mem[x]-1}
push $loop // < 0 >
jump // <> {jump to $loop}
$end:
push 2 // 2
sysc // {output a line ending}
halt // {halt the machine}
.data
// statically allocate one 32-bit word of memory
// and initialise to contain integer value 9
x: 9
```
