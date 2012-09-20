/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import java.io.File;   
import java.util.Comparator;
/**
 *
 * @author Dejan
 */
public class WindowsExplorerFileNameComparator implements Comparator<File> {
    private String file1, file2;
    private int pos1, pos2, len1, len2;

    @Override
    public int compare(File f1, File f2) {
        file1 = f1.getName();
        file2 = f2.getName();
        len1 = file1.length();
        len2 = file2.length();
        pos1 = pos2 = 0;

        int result = 0;
        while (result == 0 && pos1 < len1 && pos2 < len2) {
            char ch1 = file1.charAt(pos1);
            char ch2 = file2.charAt(pos2);

            if (Character.isDigit(ch1)) {
                result = Character.isDigit(ch2) ? compareNumbers() : -1;
            } else if (Character.isLetter(ch1)) {
                result = Character.isLetter(ch2) ? compareOther(true) : 1;
            } else {
                result = Character.isDigit(ch2) ? 1
                        : Character.isLetter(ch2) ? -1
                        : compareOther(false);
            }

            pos1++;
            pos2++;
        }

        return result == 0 ? len1 - len2 : result;
    }

    private int compareNumbers() {
        int end1 = pos1 + 1;
        while (end1 < len1 && Character.isDigit(file1.charAt(end1))) {
            end1++;
        }
        int fullLen1 = end1 - pos1;
        while (pos1 < end1 && file1.charAt(pos1) == '0') {
            pos1++;
        }

        int end2 = pos2 + 1;
        while (end2 < len2 && Character.isDigit(file2.charAt(end2))) {
            end2++;
        }
        int fullLen2 = end2 - pos2;
        while (pos2 < end2 && file2.charAt(pos2) == '0') {
            pos2++;
        }

        int delta = (end1 - pos1) - (end2 - pos2);
        if (delta != 0) {
            return delta;
        }

        while (pos1 < end1 && pos2 < end2) {
            delta = file1.charAt(pos1++) - file2.charAt(pos2++);
            if (delta != 0) {
                return delta;
            }
        }

        pos1--;
        pos2--; 

        return fullLen2 - fullLen1;
    }

    private int compareOther(boolean isLetters) {
        char ch1 = file1.charAt(pos1);
        char ch2 = file2.charAt(pos2);

        if (ch1 == ch2) {
            return 0;
        }

        if (isLetters) {
            ch1 = Character.toUpperCase(ch1);
            ch2 = Character.toUpperCase(ch2);
            if (ch1 != ch2) {
            ch1 = Character.toLowerCase(ch1);
            ch2 = Character.toLowerCase(ch2);
            }
        }

        return ch1 - ch2;
    }
}
