"foo"
r4 in String
freed
"bar"
r4 in String
r5 in Ident
freed
freed
	.data


	msg_1:
		.word 6
		.ascii "bar"
	msg_0:
		.word 6
		.ascii "foo"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =L0
		STR r4, [sp, #0]
		LDR r4, =L1
		LDR r5, [sp, #0]
		STR r4, [r5, ]
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}

