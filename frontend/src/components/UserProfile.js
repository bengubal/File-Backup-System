import React, { useState, useEffect } from "react";
import axios from "axios";

const UserProfile = () => {
  const [userId, setUserId] = useState(null);
  const [userDetails, setUserDetails] = useState(null);
  const [newPassword, setNewPassword] = useState("");
  const [files, setFiles] = useState([]);
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [friendId, setFriendId] = useState("");
  const [friendsList, setFriendsList] = useState([]);
  const [sharedFiles, setSharedFiles] = useState([]);
  const [selectedFileForShare, setSelectedFileForShare] = useState("");
  const [selectedFriendForShare, setSelectedFriendForShare] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      setUserId(storedUserId);
      setLoading(false);
    } else {
      setMessage("Giriş bilgileri bulunamadı. Lütfen giriş yapın.");
      setLoading(false);
    }
  }, []);

  const getUserDetails = async () => {
    if (!userId) return;
    try {
      const response = await axios.get(`/fms/user/${userId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setUserDetails(response.data);
    } catch (error) {
      console.error("Kullanıcı bilgisi alınamadı:", error);
      setMessage("Kullanıcı bilgileri alınırken hata oluştu.");
    }
  };

  useEffect(() => {
    if (userId) {
      getUserDetails();
    }
  }, [userId]);

  const getFiles = async () => {
    if (!userId) return;
    try {
      const response = await axios.get(`/fms/files/list/${userId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setFiles(response.data);
    } catch (error) {
      console.error("Dosyalar getirilemedi:", error);
      setMessage("Dosyalar alınırken hata oluştu.");
    }
  };

  const fetchSharedFiles = async () => {
    if (!userId) return;
    try {
      const response = await axios.get(`/fms/files/shared/${userId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setSharedFiles(response.data);
    } catch (error) {
      console.error("Paylaşılan dosyalar getirilemedi:", error);
      setMessage("Paylaşılan dosyalar getirilirken hata oluştu.");
    }
  };

  useEffect(() => {
    if (userId) {
      getFiles();
      getFriendsList();
      fetchSharedFiles();
    }
  }, [userId]);

  const handlePasswordChange = async () => {
    if (!userId) {
      setMessage("Şifre değiştirmek için kullanıcı ID gerekli.");
      return;
    }
    try {
      await axios.post(`/fms/user/${userId}/password-change-request`, null, {
        params: { newPassword },
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setMessage("Şifre değiştirme isteği başarıyla gönderildi.");
      setNewPassword("");
    } catch (error) {
      console.error("Şifre değiştirilemedi:", error);
      setMessage("Şifre değiştirilemedi.");
    }
  };

  const handleFileUpload = async () => {
    if (!userId) {
      setMessage("Dosya yüklemek için kullanıcı ID gerekli.");
      return;
    }
    const formData = new FormData();
    formData.append("file", file);

    try {
      await axios.post(`/fms/files/upload/${userId}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setMessage("Dosya başarıyla yüklendi.");
      setFile(null);
      getFiles();
    } catch (error) {
      console.error("Dosya yüklenemedi:", error);
      setMessage("Dosya yüklenemedi.");
    }
  };

  const handleFileDownload = async (fileName) => {
    if (!userId) {
      setMessage("Dosya indirmek için kullanıcı ID gerekli.");
      return;
    }
    try {
      const response = await axios.get(
        `/fms/files/download/${userId}/${fileName}`,
        {
          responseType: "blob",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", fileName);
      document.body.appendChild(link);
      link.click();
      setMessage("Dosya başarıyla indirildi.");
    } catch (error) {
      console.error("Dosya indirilemedi:", error);
      setMessage("Dosya indirilemedi.");
    }
  };

  const handleShareFile = async () => {
    if (!userId || !selectedFriendForShare || !selectedFileForShare) {
      setMessage(
        "Dosya paylaşmak için kullanıcı ID, arkadaş ve dosya seçilmelidir."
      );
      return;
    }
    try {
      await axios.post(`/fms/files/share`, null, {
        params: {
          userId: userId,
          friendId: selectedFriendForShare,
          fileName: selectedFileForShare,
        },
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setMessage("Dosya başarıyla paylaşıldı.");
      setSelectedFileForShare("");
      setSelectedFriendForShare("");
      fetchSharedFiles();
    } catch (error) {
      console.error("Dosya paylaşılamadı:", error);
      setMessage("Dosya paylaşılamadı.");
    }
  };

  const handleAddFriend = async () => {
    if (!userId || !friendId) {
      setMessage("Arkadaş eklemek için kullanıcı ID ve arkadaş ID gerekli.");
      return;
    }
    try {
      const response = await axios.post(`/fms/user/addFriend`, null, {
        params: { userId, friendId },
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setMessage(response.data);
      setFriendId("");
      getFriendsList();
    } catch (error) {
      console.error("Arkadaş eklenemedi:", error);
      setMessage("Arkadaş eklenirken hata oluştu.");
    }
  };

  const getFriendsList = async () => {
    if (!userId) return;
    try {
      const response = await axios.get(`/fms/user/friends/${userId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setFriendsList(response.data);
    } catch (error) {
      console.error("Arkadaş listesi alınamadı:", error);
      setMessage("Arkadaş listesi alınırken hata oluştu.");
    }
  };
  if (loading) {
    return <div>Kullanıcı bilgileri yükleniyor...</div>;
  }

  if (!userDetails) {
    return <div>{message || "Kullanıcı bilgileri yükleniyor..."}</div>;
  }

  return (
    <div className="profile-container">
      <h1 className="profile-title">Profil Sayfası</h1>

      <div className="profile-info-section">
        <h2>Hoşgeldiniz, {userDetails.username}</h2>
        <p>
          <strong>Rol:</strong> {userDetails.role || "Belirtilmemiş"}
        </p>
        <p>
          <strong>Depolama Limiti:</strong> {userDetails.storageLimit || "0"} MB
        </p>
        <p>
          <strong>Kullanıcı ID:</strong> {userDetails.id}
        </p>
      </div>

      <div className="profile-password-section">
        <h3>Şifre Değiştir</h3>
        <input
          type="password"
          placeholder="Yeni Şifre"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
        />
        <button onClick={handlePasswordChange}>Şifreyi Değiştir</button>
      </div>

      <div className="profile-file-upload-section">
        <h3>Dosya Yükle</h3>
        <input type="file" onChange={(e) => setFile(e.target.files[0])} />
        <button onClick={handleFileUpload}>Yükle</button>
      </div>

      {/* Dosya paylaşma */}
      <div className="profile-file-share-section">
        <h3>Dosya Paylaş</h3>
        <select
          value={selectedFileForShare}
          onChange={(e) => setSelectedFileForShare(e.target.value)}
        >
          <option value="">Dosya Seç</option>
          {files.map((fileName, index) => (
            <option key={index} value={fileName}>
              {fileName}
            </option>
          ))}
        </select>
        <select
          value={selectedFriendForShare}
          onChange={(e) => setSelectedFriendForShare(e.target.value)}
        >
          <option value="">Arkadaş Seç</option>
          {friendsList.map((friend, index) => (
            <option key={index} value={friend}>
              {friend}
            </option>
          ))}
        </select>
        <button onClick={handleShareFile}>Paylaş</button>
      </div>

      <div className="profile-file-list-section">
        <h3>Dosyalarım</h3>
        {files.length > 0 ? (
          <ul>
            {files.map((fileName, index) => (
              <li key={index}>
                {fileName}{" "}
                <button onClick={() => handleFileDownload(fileName)}>
                  İndir
                </button>
              </li>
            ))}
          </ul>
        ) : (
          <p>Hiç dosya bulunamadı.</p>
        )}
      </div>
      <div className="profile-shared-files-section">
        <h3>Paylaşılan Dosyalar</h3>
        {sharedFiles.length > 0 ? (
          <ul>
            {sharedFiles.map((sharedFile, index) => (
              <li key={index}>
                {sharedFile.fileName} (Paylaşan: {sharedFile.senderUsername})
                <button onClick={() => handleFileDownload(sharedFile.fileName)}>
                  İndir
                </button>
              </li>
            ))}
          </ul>
        ) : (
          <p>Henüz dosya paylaşılmamış.</p>
        )}
      </div>

      <div className="profile-friend-add-section">
        <h3>Arkadaş Ekle</h3>
        <input
          type="text"
          placeholder="Arkadaş ID"
          value={friendId}
          onChange={(e) => setFriendId(e.target.value)}
        />
        <button onClick={handleAddFriend}>Arkadaş Ekle</button>
      </div>

      <div className="profile-friends-list-section">
        <h3>Arkadaşlar</h3>
        {friendsList.length > 0 ? (
          <ul>
            {friendsList.map((friend, index) => (
              <li key={index}>{friend}</li>
            ))}
          </ul>
        ) : (
          <p>Hiç arkadaş bulunamadı.</p>
        )}
      </div>
      {message && <p className="profile-error">{message}</p>}
    </div>
  );
};

export default UserProfile;
