r4 in pair
r5 in charNode
freed
r5 in charNode
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
		LDR r5, =39
		LDR r0, =1
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, =39
		LDR r0, =1
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #0]
		LDR r0, =0
		POP {pc}

