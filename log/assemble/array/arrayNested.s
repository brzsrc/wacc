r4 in array1
r5 in array2
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
freed
freed
r4 in array1
r5 in array2
r6 in integer
freed
r6 in integer
freed
freed
freed
r4 in array1
r5 in array2
r6 in Ident
freed
r6 in Ident
freed
freed
freed
r4 in arrayElem
r5 in arrayElem
freed
r5 in arrayElem
freed
freed
r4 in arrayElem
r5 in arrayElem
freed
r5 in arrayElem
freed
freed
	.data


	msg_0:
		.word 7
		.ascii "%p\0"
	msg_1:
		.word 5
		.ascii "\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #12
		LDR r0, =16
		BL malloc
		MOV r4, r0
		LDR r6, =1
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =2
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r6, =3
		LDR r5, [r6, ]
		STR r4, [r5, #12]
		LDR r5, =16
		STR r5, [r4, ]
		STR r4, [sp, #0]
		LDR r0, =12
		BL malloc
		MOV r4, r0
		LDR r6, =3
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =4
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r5, =12
		STR r5, [r4, ]
		STR r4, [sp, #4]
		LDR r0, =12
		BL malloc
		MOV r4, r0
		LDR r6, [sp, #0]
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, [sp, #4]
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r5, =12
		STR r5, [r4, ]
		STR r4, [sp, #8]
		ADD r4, sp, #8
		LDR r5, =0
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r5, =2
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_reference
		BL p_print_ln
		ADD r4, sp, #8
		LDR r5, =1
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r5, =0
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_reference
		BL p_print_ln
		ADD sp, sp, #12
		LDR r0, =0
		POP {pc}
	p_print_reference:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

