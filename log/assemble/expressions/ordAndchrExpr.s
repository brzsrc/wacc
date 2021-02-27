r4 in charNode
freed
r4 in integer
freed
r4 in Ident
freed
" is "
r4 in String
freed
r4 in Ident
freed
r4 in Ident
freed
" is "
r4 in String
freed
r4 in Ident
freed
	.data


	msg_2:
		.word 9
		.ascii "%.*s\0"
	msg_3:
		.word 7
		.ascii "%d\0"
	msg_4:
		.word 5
		.ascii "\0"
	msg_0:
		.word 7
		.ascii "%c\0"
	msg_1:
		.word 7
		.ascii " is "
	msg_5:
		.word 7
		.ascii " is "
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #5
		LDR r4, =39
		STRB r4, [sp, #0]
		LDR r4, =99
		STR r4, [sp, #1]
		LDRB r4, [sp, #0]
		MOV r0, r4
		BL p_print_char
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		LDRB r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, [sp, #1]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L1
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #1]
		MOV r0, r4
		BL putchar
		MOV r0, r4
		BL p_print_char
		BL p_print_ln
		ADD sp, sp, #5
		LDR r0, =0
		POP {pc}
	p_print_char:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_string:
		PUSH {lr}
		LDR r1, r0
		ADD r2, r0, #4
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_3
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

