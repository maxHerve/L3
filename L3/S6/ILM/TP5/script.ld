SECTIONS
{
  . = 0x00000;
  .text : { *(.text) }
  . = 0x00100;
  .data : { *(.data) }
  .bss : { *(.bss) }
}
