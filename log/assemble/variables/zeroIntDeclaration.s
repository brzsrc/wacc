r4 in integer
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		LDR r4, =0
		STR r4, [sp, #0]
		LDR r0, =0
		POP {pc}

