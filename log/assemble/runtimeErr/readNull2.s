freed
	.data


	msg_0:
		.word 7
		.ascii "%d\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =0
		STR r4, [sp, #0]
		BL p_read_int
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}
	p_read_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL scanf
		POP {pc}

