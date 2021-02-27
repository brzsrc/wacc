""
r4 in String
freed
	.data


	msg_0:
		.word 3
		.ascii ""
	.text


	.global main
	main:
		PUSH {lr}
		LDR r4, =L0
		STR r4, [sp, #0]
		LDR r0, =0
		POP {pc}

