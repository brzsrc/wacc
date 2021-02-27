r4 in array1
r5 in array2
r6 in charNode
freed
r6 in charNode
freed
r6 in charNode
freed
freed
freed
r4 in Ident
freed
r4 in array1
r5 in array2
r6 in charNode
freed
r6 in charNode
freed
r6 in charNode
freed
freed
r5 in Ident
freed
freed
r4 in Ident
freed
	.data


	msg_1:
		.word 5
		.ascii "\0"
	msg_0:
		.word 9
		.ascii "%.*s\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r0, =7
		BL malloc
		MOV r4, r0
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #12]
		LDR r5, =7
		STR r5, [r4, ]
		STR r4, [sp, #0]
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r0, =7
		BL malloc
		MOV r4, r0
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #12]
		LDR r5, =7
		STR r5, [r4, ]
		LDR r5, [sp, #0]
		STR r4, [r5, ]
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}
	p_print_string:
		PUSH {lr}
		LDR r1, r0
		ADD r2, r0, #4
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

