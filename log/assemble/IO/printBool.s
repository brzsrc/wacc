"True is "
r4 in String
freed
r4 in bool
freed
"False is "
r4 in String
freed
r4 in bool
freed
	.data


	msg_2:
		.word 9
		.ascii "true\0"
	msg_0:
		.word 11
		.ascii "True is "
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_4:
		.word 5
		.ascii "\0"
	msg_3:
		.word 10
		.ascii "false\0"
	msg_5:
		.word 12
		.ascii "False is "
	.text


	.global main
	main:
		PUSH {lr}
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		MOV r4, #1
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		LDR r4, =L1
		MOV r0, r4
		BL p_print_string
		MOV r4, #0
		MOV r0, r4
		BL p_print_bool
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
	p_print_bool:
		PUSH {lr}
		CMP r0, #0
		LDRNE r0, =msg_1
		LDREQ r0, =msg_2
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

