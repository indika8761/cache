package lk.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FileSystemCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {

	private final Map<K, String> objectsStorage;
	private final Path tempDir;
	private  int capacity;
	private final Logger logger = LoggerFactory.getLogger(FileSystemCache.class);
	
	FileSystemCache() throws IOException  {
		this.tempDir = Files.createTempDirectory("cache");
		this.tempDir.toFile().deleteOnExit();
	
		this.objectsStorage = new ConcurrentHashMap<>();
	}

	FileSystemCache(int capacity) throws IOException {
		this.tempDir = Files.createTempDirectory("cache");
		this.tempDir.toFile().deleteOnExit();
		this.capacity = capacity;
		this.objectsStorage = new ConcurrentHashMap<>(capacity);
	}

	@Override
	public void put(K key, V value) {
		File tmpFile;
		try {
			tmpFile = Files.createTempFile(tempDir, "", "").toFile();
			try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(tmpFile))) {
				outputStream.writeObject(value);
				outputStream.flush();
				objectsStorage.put(key, tmpFile.getName());
			} catch (IOException e) {
				logger.error("Can't write an object to a file " + tmpFile.getName() + ": " + e.getMessage());
			}
		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}

	@Override
	public V get(K key) {

		if (isObjectPresent(key)) {
			String fileName = objectsStorage.get(key);
			try (FileInputStream fileInputStream = new FileInputStream(new File(tempDir + File.separator + fileName));
					ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
				@SuppressWarnings("unchecked")
				V readObject = (V) objectInputStream.readObject();
				return readObject;
			} catch (ClassNotFoundException | IOException e) {
				logger.error(String.format("Can't read a file. %s: %s", fileName, e.getMessage()));
			}
		}
		logger.debug(String.format("Object with key '%s' does not exist", key));
		return null;
	}

	@Override
	public void remove(K key) {
		String fileName = objectsStorage.get(key);
		File deletedFile = new File(tempDir + File.separator + fileName);
		if (deletedFile.delete()) {
			logger.debug(String.format("Cache file '%s' has been deleted", fileName));
		} else {
			logger.debug(String.format("Can't delete a file %s", fileName));
		}
		objectsStorage.remove(key);

	}

	@Override
	public int size() {
		return objectsStorage.size();
	}

	@Override
	public boolean isObjectPresent(K key) {
		return objectsStorage.containsKey(key);
	}

	@Override
	public boolean hasEmptyPlace() {
		return size() < this.capacity;
	}

	@Override
	public void clear() {
		try {
			Files.walk(tempDir).filter(Files::isRegularFile).map(Path::toFile).forEach(file -> {
				if (file.delete()) {
					logger.debug(String.format("Cache file '%s' has been deleted", file));
				} else {
					logger.error(String.format("Can't delete a file %s", file));
				}
			});
		} catch (IOException e) {
			logger.error("Can't write an object to a file " + tempDir + ": " + e.getMessage());
		}
		objectsStorage.clear();

	}

}
