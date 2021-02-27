r4 in bool
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		B backend.instructions.Label@6fdb1f78
	L1:
	L0:
		MOV r4, #0
		B backend.instructions.Label@46f5f779
		LDR r0, =0
		POP {pc}

