#include "HT66F2390.h"



void i2c_init();
void i2c_start();
void i2c_stop();
unsigned char i2c_write1(unsigned char);
unsigned char i2c_read1(unsigned char);
unsigned char i2c_write2(unsigned int);
unsigned char i2c_read2(unsigned char);
void i2c_writeData_ra1(unsigned char,unsigned char,unsigned char);
unsigned char addressable2(unsigned char);
void i2c_writeData2(unsigned char, unsigned char);
unsigned char i2c_readData(unsigned char, unsigned char);
unsigned char *i2c_EEPROM_readData_1(unsigned char);
void i2c_EEPROM_WriteData_1(unsigned char, unsigned char[]);
unsigned char *i2c_EEPROM_readData_2(unsigned char);
void i2c_EEPROM_WriteData_2(unsigned char, unsigned char[]);
void delay(int);
void Bluetoothsend(unsigned char data[],int size);
void Bluetoothsend1(unsigned char data[],int size);
void gpioinit();
void Car_Open();
void Car_Close();
void Find_Car();
void Login();
void SetUserModeZero();
void Anti_theft();
void Anti_Open();
void Anti_Close();
void Key_Lock_Open();
void Key_Lock_Close();
void ChangePassWord();
void send2(unsigned long int);