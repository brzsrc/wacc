r4 in array1
r5 in array2
freed
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		LDR r0, =4
		BL malloc
		MOV r4, r0
		LDR r5, =4
		STR r5, [r4, ]
		STR r4, [sp, #0]
		LDR r0, =0
		POP {pc}

