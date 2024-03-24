import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Interpreter {
  static int PC; //program counter to point to the next instruction
  static int[] registers = new int[10]; //registers array
  static int instr; //a holding register for the current instruction
  static boolean run_bit = true; //runs until turned off to halt the machine
  static int instructionCounter = 0; //to keep track of instructions executed

  public static void interpret(int[] memory) {   //executing function
    PC = 0; //PC starts at zero

    while (run_bit) {
      instr = memory[PC]; //fetch next instruction into instr
      int instrType = instr / 100; //get type
      int d = (instr / 10) % 10; //get second digit
      int n = instr % 10; //gets the last digit
      int s = instr % 10; //this is for operations that involve two registers
      PC++; // increment the program counter, so it points to the next instruction

      switch (instrType) {
        case 1: //halt
          run_bit = false;
          break;
        case 2: //set register d to n
          registers[d] = n;
          break;
        case 3: //multiply register d by n
          registers[d] = (registers[d] * n) % 1000;
          break;
        case 4: //add n to register d
          registers[d] = (registers[d] + n) % 1000;
          break;
        case 5: //set register d to the value in register s
          registers[d] = registers[s];
          break;
        case 6: //multiply register d by the value in register s
          registers[d] = (registers[d] * registers[s]) % 1000;
          break;
        case 7: //add the value in register s to register d
          registers[d] = (registers[d] + registers[s]) % 1000;
          break;
        case 8: //set register d to the value in RAM whose address is in register a
          registers[d] = memory[registers[n]];
          break;
        case 9: //set the value in RAM at address in register a to that of register s
          memory[registers[n]] = registers[s];
          break;
        case 0: //go to the location in register d unless register s contains 0
          if (registers[s] != 0) {
            PC = registers[d];
          }
          break;
        default:
          System.out.println("Unknown instruction: " + instrType);
          run_bit = false; //halt
          break;
      }
      instructionCounter++; //increment the count on instructions
    }

    //outputting the registers and the instruction count
    for (int i = 0; i < registers.length; i++) {
      System.out.println("Register " + i + ": " + registers[i]);
    }
    System.out.println("Instructions Executed: " + instructionCounter);
  }

  public static void main(String[] args) {
    int[] memory = new int[1000]; //memory for storing operations
    String filePath = "input2.txt"; //filePath for either input1.txt or input2.txt

    try {
      Scanner scanner = new Scanner(new File(filePath)); //simple scanner to read the file with filePath
      int address = 0;
      while (scanner.hasNextInt() && address < memory.length) {
        memory[address++] = scanner.nextInt();
      }
    } catch (FileNotFoundException e) {
      System.out.println("Input file not found: " + filePath);
      return;
    }

    interpret(memory); //perform the execution and get results
  }
}
