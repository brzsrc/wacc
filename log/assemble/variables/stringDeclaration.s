"Hello World!"
r4 in String
freed
	.data


	msg_0:
		.word 15
		.ascii "Hello World!"
	.text


	.global main
	main:
		PUSH {lr}
		LDR r4, =L0
		STR r4, [sp, #0]
		LDR r0, =0
		POP {pc}

