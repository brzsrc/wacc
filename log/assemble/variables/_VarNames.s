r4 in integer
freed
r4 in Ident
	.data


	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =19
		STR r4, [sp, #0]
		LDR r4, [sp, #0]
		MOV r0, r4
		BL exit
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}

