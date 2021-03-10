package utils.backend.register;

/**
 * Internal labeling of register under the Intel x86-64 ISA
 * Convention:
 * %rax is used for return value
 * %rdi, %rsi, %rdx, %rcx, %r8, %r9 are used to store the first six arguemnts of a function
 * %rbp is used as the frame pointer to retain the relative position of the stack
 * %rsp is the stack pointer, which can be altered in stack frame call(i.e. function)
 * %rip is the instruction pointer, used in accessing constant/string literals
 */
public enum IntelRegisterLabel {
    RAX, RBX, RCX, RDX, RSI, RDI, RBP, RSP, R8, R9, R10, R11, R12, R13, R14, R15, RIP
}
