import org.example.DB.ConnectionManagerSingleton;
import org.example.DB.DBClient;
import org.example.GUI.DBForm.DBConnectionForm;
import org.example.GUI.Membership.AuthencationForm;
import org.example.GUI.Membership.LoginForm;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

	public static void main(String[] args) throws Exception{
		Class.forName("org.sqlite.JDBC");
		boolean isLogin = false;
		AuthencationForm authencationForm = new LoginForm();
		while (true) {
			if (authencationForm.createForm()) {
				isLogin = true;
				break;
			} else {
				int retry = JOptionPane.showConfirmDialog(
					null,
					"Are you exit?",
					"Exit",
					JOptionPane.YES_NO_OPTION
				);
				if (retry == JOptionPane.YES_OPTION) {
					System.out.println("User chose to exit.");
					break;
				}
			}
		}

		if (isLogin) {
			AtomicReference<DBClient> connectionRef = new AtomicReference<>();
			DBConnectionForm.createForm(connectionRef);
			ConnectionManagerSingleton.getInstance().closeConnection();
			ConnectionManagerSingleton.setConnetion(connectionRef.get());

			List<person> itemsList = ConnectionManagerSingleton.getInstance().getConnection()
					.getAllDataTable("person", person.class);

			CrudForm<person> form = new CrudForm<>(
				"Quản lý dữ liệu",
				person.class,
				new AddStrategy<>(),
				new EditStrategy<>(),
				new DeleteStrategy<>()
			);

			form.loadData(itemsList);
			form.setVisible(true);
		}
	}
}
