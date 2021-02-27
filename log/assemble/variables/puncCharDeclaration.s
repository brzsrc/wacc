r4 in charNode
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		LDR r4, =39
		STRB r4, [sp, #0]
		LDR r0, =0
		POP {pc}

