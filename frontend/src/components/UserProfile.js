import React, { useState, useEffect } from "react";
import axios from "axios";

const UserProfile = () => {
  const [userId, setUserId] = useState(null); // LocalStorage'dan alınan userId
  const [userDetails, setUserDetails] = useState(null); // Kullanıcı detayları
  const [newPassword, setNewPassword] = useState(""); // Yeni şifre
  const [files, setFiles] = useState([]); // Kullanıcının dosyaları
  const [file, setFile] = useState(null); // Yüklenecek dosya
  const [message, setMessage] = useState(""); // Durum mesajı

  // LocalStorage'dan userId'yi al
  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      setUserId(storedUserId);
    } else {
      setMessage("Giriş bilgileri bulunamadı. Lütfen giriş yapın.");
    }
  }, []);

  // Kullanıcı bilgilerini getir
  const getUserDetails = async () => {
    try {
      const response = await axios.get(`/fms/user/${userId}`);
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

  // Kullanıcının dosyalarını getir
  const getFiles = async () => {
    if (!userId) return; // userId mevcut değilse işlemi durdur
    try {
      const response = await axios.get(`/fms/files/list/${userId}`);
      setFiles(response.data);
    } catch (error) {
      console.error("Dosyalar getirilemedi:", error);
      setMessage("Dosyalar alınırken hata oluştu.");
    }
  };

  useEffect(() => {
    if (userId) {
      getFiles();
    }
  }, [userId]);

  // Şifre değiştirme isteği gönder
  const handlePasswordChange = async () => {
    if (!userId) {
      setMessage("Şifre değiştirmek için kullanıcı ID gerekli.");
      return;
    }
    try {
      await axios.post(`/fms/user/${userId}/password-change-request`, null, {
        params: { newPassword },
      });
      setMessage("Şifre değiştirme isteği başarıyla gönderildi.");
    } catch (error) {
      console.error("Şifre değiştirilemedi:", error);
      setMessage("Şifre değiştirilemedi.");
    }
  };

  // Dosya yükleme
  const handleFileUpload = async () => {
    if (!userId) {
      setMessage("Dosya yüklemek için kullanıcı ID gerekli.");
      return;
    }
    const formData = new FormData();
    formData.append("file", file);

    try {
      await axios.post(`/fms/files/upload/${userId}`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      setMessage("Dosya başarıyla yüklendi.");
      getFiles(); // Dosyaları yenile
    } catch (error) {
      console.error("Dosya yüklenemedi:", error);
      setMessage("Dosya yüklenemedi.");
    }
  };

  // Dosya indirme
  const handleFileDownload = async (fileName) => {
    if (!userId) {
      setMessage("Dosya indirmek için kullanıcı ID gerekli.");
      return;
    }
    try {
      const response = await axios.get(
        `/fms/files/download/${userId}/${fileName}`,
        { responseType: "blob" } // Blob formatında yanıt
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

  // Kullanıcı bilgileri ve dosya yükleme/bulma durumu
  if (!userDetails) {
    return <div>{message || "Kullanıcı bilgileri yükleniyor..."}</div>;
  }

  return (
    <div>
      <h1>Profil Sayfası</h1>

      {/* Kullanıcı bilgileri */}
      <div>
        <h2>Hoşgeldiniz, {userDetails.username}</h2>

        <p>
          <strong>Rol:</strong> {userDetails.role || "Belirtilmemiş"}
        </p>
        <p>
          <strong>Depolama Limiti:</strong> {userDetails.storageLimit || "0"} MB
        </p>
        <p>
          <strong>Takımlar:</strong>{" "}
          {userDetails.teams && userDetails.teams.length > 0
            ? userDetails.teams.join(", ")
            : "Henüz bir takım yok"}
        </p>
        <p>
          <strong>Kullanıcı ID:</strong> {userDetails.id}
        </p>
      </div>

      {/* Şifre değiştirme */}
      <div>
        <h3>Şifre Değiştir</h3>
        <input
          type="password"
          placeholder="Yeni Şifre"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
        />
        <button onClick={handlePasswordChange}>Şifreyi Değiştir</button>
      </div>

      {/* Dosya yükleme */}
      <div>
        <h3>Dosya Yükle</h3>
        <input type="file" onChange={(e) => setFile(e.target.files[0])} />
        <button onClick={handleFileUpload}>Yükle</button>
      </div>

      {/* Kullanıcının dosyaları */}
      <div>
        <h3>Dosyalar</h3>
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

      {/* Durum mesajı */}
      {message && <p>{message}</p>}
    </div>
  );
};

export default UserProfile;
