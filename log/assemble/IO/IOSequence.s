r4 in integer
freed
"Please input an integer: "
r4 in String
freed
"You input: "
r4 in String
freed
r4 in Ident
freed
	.data


	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_3:
		.word 14
		.ascii "You input: "
	msg_4:
		.word 7
		.ascii "%d\0"
	msg_0:
		.word 28
		.ascii "Please input an integer: "
	msg_5:
		.word 5
		.ascii "\0"
	msg_2:
		.word 7
		.ascii "%d\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =0
		STR r4, [sp, #0]
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		BL p_read_int
		LDR r4, =L1
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
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
	p_read_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL scanf
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

