r4 in bool
freed
r4 in bool
r5 in Ident
freed
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #1
		MOV r4, #0
		STRB r4, [sp, #0]
		MOV r4, #1
		LDRB r5, [sp, #0]
		STR r4, [r5, ]
		ADD sp, sp, #1
		LDR r0, =0
		POP {pc}

