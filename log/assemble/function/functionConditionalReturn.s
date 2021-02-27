r4 in bool
freed
r4 in funcCall
freed
r4 in Ident
freed
	.data


	msg_2:
		.word 5
		.ascii "\0"
	msg_1:
		.word 10
		.ascii "false\0"
	msg_0:
		.word 9
		.ascii "true\0"
	.text


	.global main
		MOV r4, #1
		B backend.instructions.Label@629f0666
		B backend.instructions.Label@1bc6a36e
	L0:
	L1:
	main:
		PUSH {lr}
		SUB sp, sp, #1
		BL f_f
		ADD sp, sp, #0
		MOV r0, r4
		STRB r4, [sp, #0]
		LDRB r4, [sp, #0]
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		ADD sp, sp, #1
		LDR r0, =0
		POP {pc}
	p_print_bool:
		PUSH {lr}
		CMP r0, #0
		LDRNE r0, =msg_0
		LDREQ r0, =msg_1
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

