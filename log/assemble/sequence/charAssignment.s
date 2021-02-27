r4 in charNode
freed
r4 in charNode
r5 in Ident
freed
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #1
		LDR r4, =39
		STRB r4, [sp, #0]
		LDR r4, =39
		LDRB r5, [sp, #0]
		STR r4, [r5, ]
		ADD sp, sp, #1
		LDR r0, =0
		POP {pc}

