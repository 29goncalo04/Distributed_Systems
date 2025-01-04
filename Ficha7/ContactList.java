import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

// class ContactList extends ArrayList<Contact> {

//     public void serialize(DataOutputStream out) throws IOException {
//         out.writeInt(this.size());
//         for(Contact c : this) c.serialize(out);
//     }

//     public static ContactList deserialize(DataInputStream in) throws IOException {
//         ContactList contactos = new ContactList();
//         int nContactos = in.readInt();
//         for(int i = 0; i<nContactos; i++){
//             Contact c = Contact.deserialize(in);
//             contactos.add(c);
//         }
//         return contactos;
//     }
// }



class ContactList extends ArrayList<Contact> {

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(this.size());
        for(Contact c : this) c.serialize(out);
    }

    public static ContactList deserialize(DataInputStream in) throws IOException {
        int nContactos = in.readInt();
        ContactList contacts = new ContactList();
        for(int i = 0; i<nContactos; i++) contacts.add(Contact.deserialize(in));
        return contacts;
    }

}