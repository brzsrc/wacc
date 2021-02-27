", "
r4 in String
freed
r4 in integer
freed
r4 in charNode
freed
r4 in bool
freed
"this is a string"
r4 in String
freed
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
r6 in charNode
freed
r6 in charNode
freed
r6 in charNode
freed
freed
freed
r4 in array1
r5 in array2
r6 in bool
freed
r6 in bool
freed
r6 in bool
freed
freed
freed
r4 in array1
r5 in array2
"array"
r6 in String
freed
"of"
r6 in String
freed
"strings"
r6 in String
freed
freed
freed
r4 in pair
r5 in integer
freed
r5 in integer
freed
freed
r4 in pair
r5 in charNode
freed
r5 in bool
freed
freed
r4 in pair
r5 in charNode
freed
r5 in bool
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
r6 in charNode
freed
r6 in charNode
freed
r6 in charNode
freed
freed
freed
r4 in pair
r5 in Ident
freed
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in Ident
freed
"( ["
r4 in String
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
"] , ["
r4 in String
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
"] )"
r4 in String
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in Ident
freed
"[ "
r4 in String
freed
r4 in Ident
freed
" = ("
r4 in String
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
"), "
r4 in String
freed
r4 in Ident
freed
" = ("
r4 in String
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
") ]"
r4 in String
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in arrayElem
r5 in arrayElem
freed
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
	.data


	msg_5:
		.word 6
		.ascii "( ["
	msg_11:
		.word 5
		.ascii "\0"
	msg_15:
		.word 9
		.ascii "true\0"
	msg_17:
		.word 6
		.ascii "), "
	msg_14:
		.word 7
		.ascii " = ("
	msg_16:
		.word 10
		.ascii "false\0"
	msg_13:
		.word 7
		.ascii "%p\0"
	msg_12:
		.word 5
		.ascii "[ "
	msg_18:
		.word 7
		.ascii " = ("
	msg_0:
		.word 5
		.ascii ", "
	msg_1:
		.word 19
		.ascii "this is a string"
	msg_8:
		.word 8
		.ascii "] , ["
	msg_2:
		.word 8
		.ascii "array"
	msg_6:
		.word 9
		.ascii "%.*s\0"
	msg_10:
		.word 6
		.ascii "] )"
	msg_19:
		.word 6
		.ascii ") ]"
	msg_7:
		.word 7
		.ascii "%d\0"
	msg_3:
		.word 5
		.ascii "of"
	msg_9:
		.word 7
		.ascii "%c\0"
	msg_4:
		.word 10
		.ascii "strings"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #8
		LDR r4, =L0
		STR r4, [sp, #0]
		LDR r4, =5
		STR r4, [sp, #4]
		SUB sp, sp, #1
		LDR r4, =39
		STRB r4, [sp, #8]
		SUB sp, sp, #1
		MOV r4, #1
		STRB r4, [sp, #9]
		SUB sp, sp, #4
		LDR r4, =L1
		STR r4, [sp, #10]
		SUB sp, sp, #16
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
		STR r4, [sp, #14]
		SUB sp, sp, #4
		LDR r0, =7
		BL malloc
		MOV r4, r0
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #12]
		LDR r5, =7
		STR r5, [r4, ]
		STR r4, [sp, #18]
		SUB sp, sp, #4
		LDR r0, =7
		BL malloc
		MOV r4, r0
		MOV r6, #1
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		MOV r6, #0
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		MOV r6, #1
		LDR r5, [r6, ]
		STR r4, [r5, #12]
		LDR r5, =7
		STR r5, [r4, ]
		STR r4, [sp, #22]
		SUB sp, sp, #16
		LDR r0, =16
		BL malloc
		MOV r4, r0
		LDR r6, =L2
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =L3
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r6, =L4
		LDR r5, [r6, ]
		STR r4, [r5, #12]
		LDR r5, =16
		STR r5, [r4, ]
		STR r4, [sp, #26]
		SUB sp, sp, #12
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =1
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, =2
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #30]
		SUB sp, sp, #24
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =39
		LDR r0, =1
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		MOV r5, #1
		LDR r0, =1
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #34]
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =39
		LDR r0, =1
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		MOV r5, #0
		LDR r0, =1
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #38]
		LDR r0, =12
		BL malloc
		MOV r4, r0
		LDR r6, [sp, #34]
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, [sp, #38]
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r5, =12
		STR r5, [r4, ]
		STR r4, [sp, #42]
		SUB sp, sp, #20
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
		STR r4, [sp, #46]
		LDR r0, =7
		BL malloc
		MOV r4, r0
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r6, =39
		LDR r5, [r6, ]
		STR r4, [r5, #12]
		LDR r5, =7
		STR r5, [r4, ]
		STR r4, [sp, #50]
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, [sp, #46]
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, [sp, #50]
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #54]
		LDR r4, [sp, #54]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		STR r4, [sp, #58]
		LDR r4, [sp, #54]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [sp, #62]
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
		ADD r4, sp, #58
		LDR r5, =0
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_int
		LDR r4, [sp, #102]
		MOV r0, r4
		BL p_print_string
		ADD r4, sp, #58
		LDR r5, =1
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_int
		LDR r4, [sp, #102]
		MOV r0, r4
		BL p_print_string
		ADD r4, sp, #58
		LDR r5, =2
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L6
		MOV r0, r4
		BL p_print_string
		ADD r4, sp, #62
		LDR r5, =0
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_char
		LDR r4, [sp, #102]
		MOV r0, r4
		BL p_print_string
		ADD r4, sp, #62
		LDR r5, =1
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_char
		LDR r4, [sp, #102]
		MOV r0, r4
		BL p_print_string
		ADD r4, sp, #62
		LDR r5, =2
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_char
		LDR r4, =L7
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #20
		ADD r4, sp, #42
		LDR r5, =0
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		STR r4, [sp, #66]
		LDR r4, [sp, #66]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		STRB r4, [sp, #70]
		LDR r4, [sp, #66]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STRB r4, [sp, #71]
		ADD r4, sp, #42
		LDR r5, =1
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		STR r4, [sp, #72]
		LDR r4, [sp, #72]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		STRB r4, [sp, #76]
		LDR r4, [sp, #72]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STRB r4, [sp, #77]
		LDR r4, =L8
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #66]
		MOV r0, r4
		BL p_print_reference
		LDR r4, =L9
		MOV r0, r4
		BL p_print_string
		LDRB r4, [sp, #70]
		MOV r0, r4
		BL p_print_char
		LDR r4, [sp, #82]
		MOV r0, r4
		BL p_print_string
		LDRB r4, [sp, #71]
		MOV r0, r4
		BL p_print_bool
		LDR r4, =L10
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #72]
		MOV r0, r4
		BL p_print_reference
		LDR r4, =L11
		MOV r0, r4
		BL p_print_string
		LDRB r4, [sp, #76]
		MOV r0, r4
		BL p_print_char
		LDR r4, [sp, #82]
		MOV r0, r4
		BL p_print_string
		LDRB r4, [sp, #77]
		MOV r0, r4
		BL p_print_bool
		LDR r4, =L12
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #24
		LDR r4, [sp, #30]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		STR r4, [sp, #78]
		LDR r4, [sp, #30]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [sp, #82]
		LDR r4, [sp, #78]
		MOV r0, r4
		BL p_print_int
		LDR r4, [sp, #58]
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #82]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		ADD sp, sp, #12
		ADD r4, sp, #26
		LDR r5, =0
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		STR r4, [sp, #86]
		ADD r4, sp, #26
		LDR r5, =1
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		STR r4, [sp, #90]
		ADD r4, sp, #26
		LDR r5, =2
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		STR r4, [sp, #94]
		LDR r4, [sp, #86]
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #46]
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #90]
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #46]
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #94]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #16
		ADD r4, sp, #22
		LDR r5, =0
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_bool
		LDR r4, [sp, #30]
		MOV r0, r4
		BL p_print_string
		ADD r4, sp, #22
		LDR r5, =1
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_bool
		LDR r4, [sp, #30]
		MOV r0, r4
		BL p_print_string
		ADD r4, sp, #22
		LDR r5, =2
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		ADD sp, sp, #4
		LDR r4, [sp, #18]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #4
		ADD r4, sp, #14
		LDR r5, =0
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		STR r4, [sp, #98]
		ADD r4, sp, #14
		LDR r5, =1
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		STR r4, [sp, #102]
		ADD r4, sp, #14
		LDR r5, =2
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		STR r4, [sp, #106]
		LDR r4, [sp, #98]
		MOV r0, r4
		BL p_print_int
		LDR r4, [sp, #22]
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #102]
		MOV r0, r4
		BL p_print_int
		LDR r4, [sp, #22]
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #106]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		ADD sp, sp, #16
		LDR r4, [sp, #10]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #4
		LDRB r4, [sp, #9]
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		ADD sp, sp, #1
		LDRB r4, [sp, #8]
		MOV r0, r4
		BL p_print_char
		BL p_print_ln
		ADD sp, sp, #1
		LDR r4, [sp, #4]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		ADD sp, sp, #8
		LDR r0, =0
		POP {pc}
	p_print_string:
		PUSH {lr}
		LDR r1, r0
		ADD r2, r0, #4
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_char:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_3
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_reference:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_4
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_bool:
		PUSH {lr}
		CMP r0, #0
		LDRNE r0, =msg_5
		LDREQ r0, =msg_6
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}

