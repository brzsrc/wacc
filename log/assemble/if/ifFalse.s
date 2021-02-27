r4 in bool
freed
"here"
r4 in String
freed
"not here"
r4 in String
freed
	.data


	msg_3:
		.word 11
		.ascii "not here"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_2:
		.word 5
		.ascii "\0"
	msg_0:
		.word 7
		.ascii "here"
	.text


	.global main
	main:
		PUSH {lr}
		MOV r4, #0
		B backend.instructions.Label@1c2c22f3
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		B backend.instructions.Label@f5f2bb7
	L0:
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
	L1:
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

