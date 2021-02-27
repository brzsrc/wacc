"Hello World!\n"
r4 in String
freed
	.data


	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_0:
		.word 17
		.ascii "Hello World!\n"
	.text


	.global main
	main:
		PUSH {lr}
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
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

