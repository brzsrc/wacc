r4 in bool
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		MOV r4, #1
		B backend.instructions.Label@5b1d2887
		B backend.instructions.Label@46f5f779
	L0:
	L1:
		LDR r0, =0
		POP {pc}

