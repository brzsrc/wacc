r4 in pair
r5 in integer
freed
r5 in integer
freed
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =10
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, =3
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #0]
		LDR r0, =0
		POP {pc}

