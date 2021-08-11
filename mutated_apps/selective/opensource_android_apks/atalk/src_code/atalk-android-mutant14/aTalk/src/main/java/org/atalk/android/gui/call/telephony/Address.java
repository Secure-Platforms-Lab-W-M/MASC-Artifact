package org.atalk.android.gui.call.telephony;

import android.text.TextUtils;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;

import org.apache.james.mime4j.codec.EncoderUtil;
import org.jivesoftware.smack.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import androidx.annotation.VisibleForTesting;
import timber.log.Timber;

/*
 *  Implementation of Address as [mAddress, mPerson] where:
 *  mAddress = Contact Phone Number
 *  mPerson = Contact Name
 */
public class Address implements Serializable
{
    private static final Pattern ATOM = Pattern.compile("^(?:[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~]|\\s)+$");

    /**
     * Immutable empty {@link Address} array
     */
    private static final Address[] EMPTY_ADDRESS_ARRAY = new Address[0];

    private String mAddress;
    private String mPerson;

    public Address(String address, String person)
    {
        this(address, person, true);
    }

    private Address(String address, String person, boolean parse)
    {
        if (parse) {
            Rfc822Token[] tokens = Rfc822Tokenizer.tokenize(address);
            if (tokens.length > 0) {
                Rfc822Token token = tokens[0];
                mAddress = token.getAddress();
                String name = token.getName();

                /*
                 * Don't use the "person" argument if "address" is of the form:
                 * James Bond <james.bond@mi6.uk>
                 * See issue 2920
                 */
                mPerson = StringUtils.returnIfNotEmptyTrimmed(name);
                if ((mPerson == null) && (person != null))
                    mPerson = person.trim();
            }
            else {
                Timber.e("Invalid address: %s", address);
            }
        }
        else {
            mAddress = address;
            mPerson = person;
        }
    }

    public String getAddress()
    {
        return mAddress;
    }

    public void setAddress(String address)
    {
        mAddress = address;
    }

    public String getPerson()
    {
        return mPerson;
    }

    public void setPerson(String person)
    {
        mPerson = StringUtils.returnIfNotEmptyTrimmed(person);
    }

    /**
     * Parse a comma separated list of phone addresses in human readable format and return an
     * array of Address objects, RFC-822 encoded.
     *
     * @param addressList List of addresses
     * @return An array of 0 or more Addresses.
     */
    public static Address[] parseUnencoded(String addressList)
    {
        List<Address> addresses = new ArrayList<>();
        if (StringUtils.isNotEmpty(addressList)) {
            Rfc822Token[] tokens = Rfc822Tokenizer.tokenize(addressList);
            for (Rfc822Token token : tokens) {
                String address = token.getAddress();
                if (!TextUtils.isEmpty(address)) {
                    addresses.add(new Address(token.getAddress(), token.getName(), false));
                }
            }
        }
        return addresses.toArray(EMPTY_ADDRESS_ARRAY);
    }

    /**
     * Parse a comma separated list of addresses in RFC-822 format and return an array of Address objects.
     *
     * @param addressList List of addresses
     * @return An array of 0 or more Addresses.
     */
    public static Address[] parse(String addressList)
    {
        if (StringUtils.isEmpty(addressList)) {
            return EMPTY_ADDRESS_ARRAY;
        }
        // parseUnencoded(addressList);

        List<Address> addresses = new ArrayList<>();
        return addresses.toArray(EMPTY_ADDRESS_ARRAY);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;
        if (mAddress != null ? !mAddress.equals(address.mAddress) : address.mAddress != null) {
            return false;
        }
        return mPerson != null ? mPerson.equals(address.mPerson) : address.mPerson == null;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (mAddress != null) {
            hash += mAddress.hashCode();
        }
        if (mPerson != null) {
            hash += 3 * mPerson.hashCode();
        }
        return hash;
    }

    @Override
    public String toString()
    {
        if (!TextUtils.isEmpty(mPerson)) {
            return quoteAtoms(mPerson) + " <" + mAddress + ">";
        }
        else {
            return mAddress;
        }
    }

    public static String toString(Address[] addresses)
    {
        if (addresses == null) {
            return null;
        }
        return TextUtils.join(", ", addresses);
    }

    private String toEncodedString()
    {
        if (!TextUtils.isEmpty(mPerson)) {
            return EncoderUtil.encodeAddressDisplayName(mPerson) + " <" + mAddress + ">";
        }
        else {
            return mAddress;
        }
    }

    public static String toEncodedString(Address[] addresses)
    {
        if (addresses == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < addresses.length; i++) {
            sb.append(addresses[i].toEncodedString());
            if (i < addresses.length - 1) {
                sb.append(',');
            }
        }
        return sb.toString();
    }

    /**
     * Unpacks an address list previously packed with packAddressList()
     *
     * @param addressList Packed address list.
     * @return Unpacked list.
     */
    public static Address[] unpack(String addressList)
    {
        if (addressList == null) {
            return new Address[]{};
        }
        List<Address> addresses = new ArrayList<>();
        int length = addressList.length();
        int pairStartIndex = 0;
        int pairEndIndex;
        int addressEndIndex;
        while (pairStartIndex < length) {
            pairEndIndex = addressList.indexOf(",\u0000", pairStartIndex);
            if (pairEndIndex == -1) {
                pairEndIndex = length;
            }
            addressEndIndex = addressList.indexOf(";\u0000", pairStartIndex);
            String address;
            String person = null;
            if (addressEndIndex == -1 || addressEndIndex > pairEndIndex) {
                address = addressList.substring(pairStartIndex, pairEndIndex);
            }
            else {
                address = addressList.substring(pairStartIndex, addressEndIndex);
                person = addressList.substring(addressEndIndex + 2, pairEndIndex);
            }
            addresses.add(new Address(address, person, false));
            pairStartIndex = pairEndIndex + 2;
        }
        return addresses.toArray(new Address[0]);
    }

    /**
     * Packs an address list into a String that is very quick to read
     * and parse. Packed lists can be unpacked with unpackAddressList()
     * The packed list is a ",\u0000" separated list of: address;\u0000person
     *
     * @param addresses Array of addresses to pack.
     * @return Packed addresses.
     */
    public static String pack(Address[] addresses)
    {
        if (addresses == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, count = addresses.length; i < count; i++) {
            Address address = addresses[i];
            sb.append(address.getAddress());
            String person = address.getPerson();
            if (person != null) {
                sb.append(";\u0000");
                // Escape quotes in the address part on the way in
                person = person.replaceAll("\"", "\\\"");
                sb.append(person);
            }
            if (i < count - 1) {
                sb.append(",\u0000");
            }
        }
        return sb.toString();
    }

    /**
     * Quote a string, if necessary, based upon the definition of an "atom," as defined by RFC2822
     * (https://tools.ietf.org/html/rfc2822#section-3.2.4). Strings that consist purely of atoms are
     * left unquoted; anything else is returned as a quoted string.
     *
     * @param text String to quote.
     * @return Possibly quoted string.
     */
    private static String quoteAtoms(final String text)
    {
        if (ATOM.matcher(text).matches()) {
            return text;
        }
        else {
            return quoteString(text);
        }
    }

    /**
     * Ensures that the given string starts and ends with the double quote character. The string is not modified in any way except to add the
     * double quote character to start and end if it's not already there.
     * sample -> "sample"
     * "sample" -> "sample"
     * ""sample"" -> ""sample""
     * "sample"" -> "sample"
     * sa"mp"le -> "sa"mp"le"
     * "sa"mp"le" -> "sa"mp"le"
     * (empty string) -> ""
     * " -> """
     *
     * @param s
     * @return
     */

    @VisibleForTesting
    static String quoteString(String s)
    {
        if (s == null) {
            return null;
        }
        if (!s.matches("^\".*\"$")) {
            return "\"" + s + "\"";
        }
        else {
            return s;
        }
    }
}
