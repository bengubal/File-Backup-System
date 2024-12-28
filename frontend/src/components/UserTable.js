// frontend/src/components/UserTable.js
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../auth/AuthContext";

const UserTable = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);
  const { user } = useAuth();
  const [storageLimit, setStorageLimit] = useState("");
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [newUser, setNewUser] = useState({
    username: "",
    password: "",
    role: "",
    storageLimit: "",
  });
  const [showNewUserForm, setShowNewUserForm] = useState(false);

  useEffect(() => {
    const fetchUsers = async () => {
      setError(null); // Hataları temizle
      try {
        const response = await axios.get(
          `http://localhost:8080/admin/users/${user.id}`
        );
        setUsers(response.data);
      } catch (err) {
        setError("Failed to fetch users.");
        console.error("Fetch Users Error:", err);
      }
    };

    if (user && user.role === "ROLE_ADMIN") {
      fetchUsers();
    }
  }, [user]);

  const handleStorageLimitChange = (e) => {
    setStorageLimit(e.target.value);
  };
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewUser({ ...newUser, [name]: value });
  };
  const handleShowNewUserForm = () => {
    setShowNewUserForm(true);
  };
  const handleCloseNewUserForm = () => {
    setShowNewUserForm(false);
    setNewUser({ username: "", password: "", role: "", storageLimit: "" });
  };
  const handleUpdateStorageLimit = async () => {
    setError(null); // Hataları temizle
    try {
      await axios.put(
        `http://localhost:8080/admin/users/${user.id}/storage-limit/${selectedUserId}?storageLimit=${storageLimit}`
      );
      const updatedUsers = users.map((user) => {
        if (user.id === selectedUserId) {
          return { ...user, storageLimit: Number(storageLimit) };
        }
        return user;
      });
      setUsers(updatedUsers);
      setStorageLimit("");
      setSelectedUserId(null);
    } catch (err) {
      setError("Failed to update storage limit.");
      console.error("Update Storage Limit Error:", err);
    }
  };

  const handleDeleteUser = async (id) => {
    setError(null); // Hataları temizle
    try {
      await axios.delete(
        `http://localhost:8080/admin/users/${user.id}/delete/${id}`
      );
      const updatedUsers = users.filter((user) => user.id !== id);
      setUsers(updatedUsers);
    } catch (err) {
      setError("Failed to delete user.");
      console.error("Delete User Error:", err);
    }
  };
  const handleRegisterNewUser = async () => {
    setError(null); // Hataları temizle
    try {
      const response = await axios.post(
        `http://localhost:8080/admin/users/${user.id}/register/${newUser.role}?username=${newUser.username}&password=${newUser.password}&storageLimit=${newUser.storageLimit}`
      );
      setUsers([...users, response.data]);
      handleCloseNewUserForm();
    } catch (err) {
      setError("Failed to register user!");
      console.error("Register New User Error:", err);
    }
  };

  return (
    <div>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <div>
        <button onClick={handleShowNewUserForm}>Register New User</button>
        {showNewUserForm && (
          <div>
            <input
              type="text"
              placeholder="Username"
              name="username"
              value={newUser.username}
              onChange={handleInputChange}
              required
            />
            <input
              type="password"
              placeholder="Password"
              name="password"
              value={newUser.password}
              onChange={handleInputChange}
              required
            />
            <input
              type="text"
              placeholder="Role"
              name="role"
              value={newUser.role}
              onChange={handleInputChange}
              required
            />
            <input
              type="number"
              placeholder="Storage Limit"
              name="storageLimit"
              value={newUser.storageLimit}
              onChange={handleInputChange}
              required
            />
            <button onClick={handleRegisterNewUser}>Register</button>
            <button onClick={handleCloseNewUserForm}>Cancel</button>
          </div>
        )}
      </div>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Role</th>
            <th>Storage Limit</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.username}</td>
              <td>{user.role}</td>
              <td>{user.storageLimit}</td>
              <td>
                <input
                  type="number"
                  placeholder="New storage limit"
                  value={storageLimit}
                  onChange={handleStorageLimitChange}
                  onFocus={() => setSelectedUserId(user.id)}
                />
                <button
                  onClick={handleUpdateStorageLimit}
                  disabled={!selectedUserId || !storageLimit}
                >
                  Update Storage
                </button>
                <button
                  onClick={() => handleDeleteUser(user.id)}
                  disabled={user.role === "ROLE_ADMIN"}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default UserTable;
