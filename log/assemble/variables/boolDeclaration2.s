r4 in bool
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		MOV r4, #1
		STRB r4, [sp, #0]
		LDR r0, =0
		POP {pc}

