freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =0
		STR r4, [sp, #0]
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}

