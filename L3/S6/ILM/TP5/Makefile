#$(SRC).s : $(SRC).c
#	nios2-elf-gcc -S $(SRC).c

SRC = tp56-corrige

all: $(SRC).bin

$(SRC).o: $(SRC).s
	nios2-elf-as -a -o $(SRC).o $(SRC).s

$(SRC).elf:	$(SRC).o
	nios2-elf-ld -Tscript.ld -o $(SRC).elf $(SRC).o

$(SRC).bin: $(SRC).elf
	nios2-elf-objcopy -j .text -j .data -O binary $(SRC).elf $(SRC).bin

bin2hex:  bin2hex.c
	gcc -o bin2hex bin2hex.c
