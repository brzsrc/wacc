r4 in integer
"Should not print this."
r5 in String
freed
	.data


	msg_2:
		.word 5
		.ascii "\0"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_0:
		.word 25
		.ascii "Should not print this."
	.text


	.global main
	main:
		PUSH {lr}
		LDR r4, =42
		MOV r0, r4
		BL exit
		LDR r5, =L0
		MOV r0, r5
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

