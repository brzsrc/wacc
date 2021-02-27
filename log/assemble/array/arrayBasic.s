r4 in array1
r5 in array2
r6 in integer
freed
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
		LDR r6, =0
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r5, =8
		STR r5, [r4, ]
		STR r4, [sp, #0]
		LDR r0, =0
		POP {pc}

