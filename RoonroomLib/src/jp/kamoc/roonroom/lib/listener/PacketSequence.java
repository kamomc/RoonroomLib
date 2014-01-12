package jp.kamoc.roonroom.lib.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * パケットシーケンス
 * @author kamoc
 *
 */
public class PacketSequence implements Iterable<Byte> {
	private List<Byte> packetSequence = new ArrayList<Byte>();

	/**
	 * コンストラクタ
	 * @param sequence パケットを構成するバイト郡
	 */
	public PacketSequence(byte[] sequence) {
		for (byte packet : sequence) {
			this.packetSequence.add(packet);
		}
	}

	@Override
	public Iterator<Byte> iterator() {
		return packetSequence.iterator();
	}

	@Override
	public String toString() {
		return "PacketSequence" + packetSequence;
	}
}
