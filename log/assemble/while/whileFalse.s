"looping..."
r4 in String
freed
r4 in bool
freed
"end of loop"
r4 in String
freed
	.data


	msg_2:
		.word 5
		.ascii "\0"
	msg_0:
		.word 13
		.ascii "looping..."
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_3:
		.word 14
		.ascii "end of loop"
	.text


	.global main
	main:
		PUSH {lr}
		B backend.instructions.Label@2280cdac
	L1:
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
	L0:
		MOV r4, #0
		B backend.instructions.Label@73035e27
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
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

