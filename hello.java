import static cTools.KernelWrapper.*;

// java -Djava.library.path="/Users/susannebair/Desktop/SS2021/Labor/labor-2021/shell_versuch/cTools/KernelWrapper.sp" hello

public class hello {

  // static {
  //   try {
  //   	// The loadLibrary method may be used when the directory containing the shared library is in java.library.path.
  //   	System.loadLibrary("KernelWrapper");
    	
  //   } catch (UnsatisfiedLinkError e) {
  //     System.err.println("Native code library failed to load.\n" + e);
  //     System.exit(1);
  //   }
  // }
  public static void main(String[] args) {
    System.out.println("Hello, World!");
    System.out.println(System.getProperty("java.library.path"));
    exit(0);
  }
}

