import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream

/**
 * @author Arpit Agarwal <arpit.agarwal></arpit.agarwal>@lenskart.in>
 * @version $Revision 1.0 $, $Date 2021/04/19 18:12 $
 * @since 0.1.0
 */
internal object Memory {
    /**
     * Function that get the size of an object.
     *
     * @param object
     * @return Size in bytes of the object or -1 if the object
     * is null.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun sizeOf(`object`: Any?): Int {
        if (`object` == null) return -1

        // Special output stream use to write the content
        // of an output stream to an internal byte array.
        val byteArrayOutputStream = ByteArrayOutputStream()

        // Output stream that can write object
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

        // Write object and close the output stream
        objectOutputStream.writeObject(`object`)
        objectOutputStream.flush()
        objectOutputStream.close()

        // Get the byte array
        val byteArray = byteArrayOutputStream.toByteArray()

        // TODO can the toByteArray() method return a
        // null array ?
        return byteArray?.size ?: 0
    }
}